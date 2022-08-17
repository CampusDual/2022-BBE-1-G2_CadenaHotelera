package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.ontimize.hr.model.core.dao.PictureDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class PictureServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	@Mock
	private CredentialUtils credentialUtils;
	@Mock
	private EntityUtils entityUtils;
	@InjectMocks
	private PictureService service;
	@Mock
	private PictureDao pictureDao;
	@Mock
	private EntityUtils utils;
	@Mock
	private MultipartFile multi;
	
	@Test
	@DisplayName("post picture OK")
	void testPostPicture() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "s";
		String c = "c";
		
		when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s, c);
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
	}
	
	@Test
	@DisplayName("post picture no data")
	void testPostPictureNoData() throws IOException {
		
		byte [] data = null;
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "s";
		String c = "c";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s, c);
		
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.PICTURE_MANDATORY, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture wrong data")
	void testPostPictureWrongData() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/gif", data);
		String s = "s";
		String c = "c";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s, c);
		
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.PICTURE_WRONG_FORMAT, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture no name")
	void testPostPictureNoName() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "";
		String c = "c";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s, c);
		
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.PICTURE_NAME_MANDATORY, er.getMessage());
	}
	
	@Test
	@DisplayName("get picture OK")
	void testGetPicture() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		Map<String,Object> erturn = new HashMap<>();
		byte [] bdata ;
		
		
		data.put(PictureDao.ATTR_ID, 1);		
		req.put("data", data);
		
		BufferedImage bImage = ImageIO.read(new File("D:\\Fotos\\Avatar.jpg"));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bImage, "jpg", bos );
		bdata = bos.toByteArray();
		
		BytesBlock bdataO = new BytesBlock(bdata);
		
		
		
		EntityResult er = new EntityResultMapImpl();
		erturn.put(PictureDao.ATTR_PICTURE, bdataO);
		er.addRecord(erturn);
		
		when(daoHelper.query(isA(PictureDao.class), anyMap(), anyList())).thenReturn(er);
		
		ResponseEntity re = service.getPicture(req);
		
		assertEquals(HttpStatus.OK, re.getStatusCode());
	}
	
	@Test
	@DisplayName("get picture no data")
	void testGetPictureNoData() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		data.put(PictureDao.ATTR_ID, 1);		
		//req.put("data", data);
		
		
		ResponseEntity re = service.getPicture(req);
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.DATA_MANDATORY, re.getBody());
	}
	
	@Test
	@DisplayName("get picture no idPicture")
	void testGetPictureNoIdPicture() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		//data.put(PictureDao.ATTR_ID, 1);		
		req.put("data", data);
		
		
		ResponseEntity re = service.getPicture(req);
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.PICTURE_ID_MANDATORY, re.getBody());
	}
	
	@Test
	@DisplayName("get picture wrong idPicture format")
	void testGetPictureWrongIdPictureFormat() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		data.put(PictureDao.ATTR_ID, "a");		
		req.put("data", data);
		
		
		ResponseEntity re = service.getPicture(req);
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.PICTURE_ID_FORMAT, re.getBody());
	}
	
	@Test
	@DisplayName("get picture picture not exists")
	void testGetPicturePictureNotExists() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		data.put(PictureDao.ATTR_ID, 1);		
		req.put("data", data);
		
		when(daoHelper.query(isA(PictureDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		
		ResponseEntity re = service.getPicture(req);
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.PICTURE_NOT_EXISTS, re.getBody());
	}
	
	@Test
	@DisplayName("get picture array OK")
	void testGetPictureArray() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		Map<String,Object> erturn = new HashMap<>();		
		data.put(PictureDao.ATTR_ID, 1);		
		req.put("data", data);
		
		byte [] dataPic;
		BufferedImage bImage = ImageIO.read(new File("D:\\Fotos\\Avatar.jpg"));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bImage, "jpg", bos );
		dataPic = bos.toByteArray();
		
		BytesBlock bdataO = new BytesBlock(dataPic);
		
		
		
		EntityResult er = new EntityResultMapImpl();
		erturn.put(PictureDao.ATTR_PICTURE, bdataO);
		er.addRecord(erturn);
		
		when(daoHelper.query(isA(PictureDao.class), anyMap(), anyList())).thenReturn(er);
		
		EntityResult res = service.getPictureArray(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		//assertEquals(MsgLabels.HOTEL_FORMAT_LONGITUDE, er.getMessage());
	}
	
	@Test
	@DisplayName("get picture array no data")
	void testGetPictureArrayNoData() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		data.put(PictureDao.ATTR_ID, 1);		
		//req.put("data", data);
		
		
		EntityResult res = service.getPictureArray(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DATA_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("get picture array no idPicture")
	void testGetPictureArrayNoIdPicture() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		//data.put(PictureDao.ATTR_ID, 1);		
		req.put("data", data);
		
		
		EntityResult res = service.getPictureArray(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PICTURE_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("get picture array idPicture wrong format")
	void testGetPictureArrayWrongIdPictureFormat() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		data.put(PictureDao.ATTR_ID, "a");		
		req.put("data", data);
		
		
		EntityResult res = service.getPictureArray(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PICTURE_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("get picture array Picture not exists")
	void testGetPictureArrayPictureNotExists() throws IOException {
		
		Map<String,Object> req = new HashMap<>();
		Map<String,Object> data = new HashMap<>();	
		data.put(PictureDao.ATTR_ID, 1);		
		req.put("data", data);
		
		when(daoHelper.query(isA(PictureDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		
		EntityResult res = service.getPictureArray(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PICTURE_NOT_EXISTS, res.getMessage());
	}
	
	
}
