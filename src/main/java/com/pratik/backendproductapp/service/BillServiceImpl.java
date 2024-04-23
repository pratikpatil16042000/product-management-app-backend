package com.pratik.backendproductapp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pratik.backendproductapp.contants.ProductConstants;
import com.pratik.backendproductapp.entity.Bill;
import com.pratik.backendproductapp.jwt.filter.JwtAuthFilter;
import com.pratik.backendproductapp.repository.BillRepository;
import com.pratik.backendproductapp.utils.ProductUtils;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BillServiceImpl implements BillService{
	
	@Autowired
	BillRepository billRepository;
	
	@Autowired
	JwtAuthFilter jwtAuthFilter;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		log.info("generate Report");
		try {
			String fileName;
			if(validateRequestMap(requestMap)) {
				if(requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
					//logic is fileName is already generated earlier
					fileName = (String) requestMap.get("uuid");
				}else {
					//
					fileName = ProductUtils.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}
				
				String data = "Name: " + requestMap.get("name") +"\n" + "Contact Number: " +requestMap.get("contactNumber") + "\n" 
										+ "Email: " + requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod") ;
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(ProductConstants.BILL_STORED_LOCATION+"\\" + fileName +".pdf"));
				document.open();
				setRectanglePdf(document);
				
				Paragraph chunk = new Paragraph("Product Management System",getFont("Header"));
				chunk.setAlignment(Element.ALIGN_CENTER);
				document.add(chunk);
				Paragraph paragraph = new Paragraph(data +"\n \n",getFont("Data"));
				document.add(paragraph);
				
				PdfPTable table  = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);
				
				JSONArray jsonArray = ProductUtils.getJsonArrayFromString((String)requestMap.get("productDetail"));
				for(int i=0;i<jsonArray.length();i++) {
					addRow(table,ProductUtils.getMapFromJson(jsonArray.getString(i)));
				}
				document.add(table);
				
				Paragraph footer = new  Paragraph("Total: " +  requestMap.get("totalAmount") + "\n" + 
														"Thank you for visiting. Please Visit again! ",getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<>("{\"uuid\":\"" + fileName +"\"}",HttpStatus.OK);
			}
			return ProductUtils.getResponseEntity(ProductConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRow(PdfPTable table, Map<String, Object> requestMap) {
		log.info("Inside addRow ");
		table.addCell((String) requestMap.get("name"));
		table.addCell((String) requestMap.get("category"));
		table.addCell((String) requestMap.get("quantity"));
		table.addCell(Double.toString((Double) requestMap.get("price")));
		table.addCell(Double.toString((Double) requestMap.get("total")));
		
		
	}

	private void addTableHeader(PdfPTable table) {
		log.info("Inside addTableHeader()");
		Stream.of("Name","Category","Quantity","Price","Sub Total")
							.forEach(columnTitle ->{
								PdfPCell header = new PdfPCell();
								header.setBackgroundColor(BaseColor.LIGHT_GRAY);
								header.setBorderWidth(2);
								header.setPhrase(new Phrase(columnTitle));
								header.setBackgroundColor(BaseColor.YELLOW);
								header.setHorizontalAlignment(Element.ALIGN_CENTER);
								header.setVerticalAlignment(Element.ALIGN_CENTER);
								table.addCell(header);
							});
	}

	private Font getFont(String type) {
		log.info("Inside getFont()");
		switch(type) {
		case "Header" : 
			Font headerFont =FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		case "Data":
			Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont ;
		default:
			return new Font();
		}
		
	}

	private void setRectanglePdf(Document document)  throws DocumentException{
		log.info("Inside setRectanglePdf()");
		Rectangle rectangle = new Rectangle(577,825,18,15);
		rectangle.enableBorderSide(1);
		rectangle.enableBorderSide(2);
		rectangle.enableBorderSide(4);
		rectangle.enableBorderSide(8);
		rectangle.setBorderColor(BaseColor.BLACK);
		document.add(rectangle);
	}

	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid((String)requestMap.get("uuid"));
			bill.setName((String)requestMap.get("name"));
			bill.setEmail((String)requestMap.get("email"));
			bill.setContactNumber((String)requestMap.get("contactNumber"));
			bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
			bill.setTotalAmount(Integer.parseInt((String)requestMap.get("totalAmount")));
			bill.setProductDetail((String)requestMap.get("productDetail"));
			bill.setCreatedBy(jwtAuthFilter.getCurrentUser());
			billRepository.save(bill);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private boolean validateRequestMap(Map<String, Object> requestMap) {
		if(requestMap.containsKey("name") &&
			requestMap.containsKey("contactNumber") &&
			requestMap.containsKey("email") &&
			requestMap.containsKey("paymentMethod") &&
			requestMap.containsKey("productDetail") &&
			requestMap.containsKey("totalAmount")) {
			return true;
		}
	else {
		return false;
	}
	}

	@Override
	public ResponseEntity<List<Bill>> getBills() {
		List<Bill> list = new ArrayList<Bill>();
		if(jwtAuthFilter.isAdmin()) {
			list = billRepository.findAllBills();
		}else {
			list = billRepository.findBillByUserName(jwtAuthFilter.getCurrentUser());
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		log.info("Inside getPdf(): requestMap: {} ",requestMap);
		try {
			byte[] byteArray = new byte[0];
			if(!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
				return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
			}
			String filePath = ProductConstants.BILL_STORED_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
			if(ProductUtils.isFileExist(filePath)) {
				byteArray = getByteArray(filePath);
				return new ResponseEntity<byte[]>(byteArray,HttpStatus.OK);
			}else {
				requestMap.put("isGenerate", false);
				generateReport(requestMap);
				byteArray = getByteArray(filePath);
				return new ResponseEntity<byte[]>(byteArray,HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getByteArray(String filePath) throws Exception {
		File initialFile = new File(filePath);
		InputStream targetInputStream = new FileInputStream(initialFile);
		byte[] byteArray = IOUtils.toByteArray(targetInputStream);
		targetInputStream.close();
		return byteArray;
	}

	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		try {
			Optional<Bill> optional = billRepository.findById(id);
			if(!optional.isEmpty()) {
				billRepository.deleteById(id);
				return ProductUtils.getResponseEntity(ProductConstants.BILL_DELETED_SUCCESSFULLY, HttpStatus.OK);
			}
			return ProductUtils.getResponseEntity(ProductConstants.BILL_DOESNT_EXIST, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProductUtils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
	


