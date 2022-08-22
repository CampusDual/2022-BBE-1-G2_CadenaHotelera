package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
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
		String s = "{\"pic_name\":\"nombre\",\"pic_desc\":\"desc\",\"pic_htl_id\":1,\"pic_rom_typ\":1}";
		
		when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s);
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
	}
	
	@Test
	@DisplayName("post picture no hotel exists")
	void testPostPictureBadHotel() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "{\"pic_name\":\"nombre\",\"pic_desc\":\"desc\",\"pic_htl_id\":1,\"pic_rom_typ\":1}";
		
		when(daoHelper.insert(any(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_pic_htl_id"));
		
		EntityResult er = service.postPicture(mf, s);
		
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture no room type exists")
	void testPostPictureBadRoomType() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "{\"pic_name\":\"nombre\",\"pic_desc\":\"desc\",\"pic_htl_id\":1,\"pic_rom_typ\":1}";
		
		when(daoHelper.insert(any(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_pic_rom_typ"));
		
		EntityResult er = service.postPicture(mf, s);
		
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture no data")
	void testPostPictureNoData() throws IOException {
		
		byte [] data = null;
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "s";
		String c = "c";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s);
		
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.PICTURE_MANDATORY, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture wrong data")
	void testPostPictureWrongData() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/gif", data);
		String s = "s";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s);
		
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.PICTURE_WRONG_FILE_FORMAT, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture bad json grammar")
	void testPostPictureBadJsonGrammar() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "{\"pic_name\":\"nombre\";\"pic_desc\":\"desc\",\"pic_htl_id\":1,\"pic_rom_typ\":1}";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s);
		
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.BAD_DATA, er.getMessage());
	}
	
	@Test
	@DisplayName("post picture no name")
	void testPostPictureNoName() throws IOException {
		
		byte [] data = "hola".getBytes();
		
		
		MultipartFile mf = new MockMultipartFile("foto","foto.jpg", "image/jpeg", data);
		String s = "{\"pic_desc\":\"desc\",\"pic_htl_id\":1,\"pic_rom_typ\":1}";
		
		//when(daoHelper.insert(any(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		EntityResult er = service.postPicture(mf, s);
		
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
		String imageStr= "/9j/4gIcSUNDX1BST0ZJTEUAAQEAAAIMbGNtcwIQAABtbnRyUkdCIFhZWiAH3AABABkAAwApADlhY3NwQVBQTAAAAAAA"
		+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAA9tYAAQAAAADTLWxjbXMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
		+ "AAAAAAAAAAAAAAAAAApkZXNjAAAA/AAAAF5jcHJ0AAABXAAAAAt3dHB0AAABaAAAABRia3B0AAABfAAAABRyWFlaAAABk"
		+ "AAAABRnWFlaAAABpAAAABRiWFlaAAABuAAAABRyVFJDAAABzAAAAEBnVFJDAAABzAAAAEBiVFJDAAABzAAAAEBkZXNjAAA"
		+ "AAAAAAANjMgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
		+ "AAAAAAAAAAAAAAAAAAAAAAAAB0ZXh0AAAAAEZCAABYWVogAAAAAAAA9tYAAQAAAADTLVhZWiAAAAAAAAADFgAAAzMAAAKkWF"
		+ "laIAAAAAAAAG+iAAA49QAAA5BYWVogAAAAAAAAYpkAALeFAAAY2lhZWiAAAAAAAAAkoAAAD4QAALbPY3VydgAAAAAAAAAaAA"
		+ "AAywHJA2MFkghrC/YQPxVRGzQh8SmQMhg7kkYFUXdd7WtwegWJsZp8rGm/fdPD6TD////bAEMABAMDBAMDBAQDBAUEBAUGCg"
		+ "cGBgYGDQkKCAoPDRAQDw0PDhETGBQREhcSDg8VHBUXGRkbGxsQFB0fHRofGBobGv/bAEMBBAUFBgUGDAcHDBoRDxEaGhoaGho"
		+ "aGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGv/AABEIABAAGAMBIgACEQEDEQH/xAAXAAADAQAAA"
		+ "AAAAAAAAAAAAAAABQYE/8QAIxAAAQQBBAIDAQAAAAAAAAAAAQIDBAURAAYSISIxExQyQf/EABUBAQEAAAAAAAAAAAAAAAAAAAQF"
		+ "/8QAHBEAAQQDAQAAAAAAAAAAAAAAAQACAxEhIjFB/9oADAMBAAIRAxEAPwBZs2ZXT9zRlv3Ehqgg2EiKHniXHWWopBwhIJOCVto"
		+ "Tjx5L/mttls21jb2iBf2a+ljvmxlMSiyqTwcdUsBQS4S2pOfJseSfL9dAxldtKRDFS1XwJzD0KDDZV9cgKEl8uSZAPfpKpEdtRy"
		+ "cfAfeNNqSg3dWyotmqmfbR95UlyaJsdKilx1aclH6CiQodk4KST7A1NsS1i05ugOfFRXztVA3BDRfulD9q+ioYitKSRDY4/I22e"
		+ "WCC5wClEjtXFPpGjU5Mq4Naq6kWlJZWjoZXKCi6264pxl48ChR6+XhKdUD1lKOsEDRp7ZKHEBzLPV//2Q==";
		BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imageStr.getBytes())));
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
		
		String imageStr= "/9j/4gIcSUNDX1BST0ZJTEUAAQEAAAIMbGNtcwIQAABtbnRyUkdCIFhZWiAH3AABABkAAwApADlhY3NwQVBQTAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAA9tYAAQAAAADTLWxjbXMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAApkZXNjAAAA/AAAAF5jcHJ0AAABXAAAAAt3dHB0AAABaAAAABRia3B0AAABfAAAABRyWFlaAAABk"
				+ "AAAABRnWFlaAAABpAAAABRiWFlaAAABuAAAABRyVFJDAAABzAAAAEBnVFJDAAABzAAAAEBiVFJDAAABzAAAAEBkZXNjAAA"
				+ "AAAAAAANjMgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
				+ "AAAAAAAAAAAAAAAAAAAAAAAAB0ZXh0AAAAAEZCAABYWVogAAAAAAAA9tYAAQAAAADTLVhZWiAAAAAAAAADFgAAAzMAAAKkWF"
				+ "laIAAAAAAAAG+iAAA49QAAA5BYWVogAAAAAAAAYpkAALeFAAAY2lhZWiAAAAAAAAAkoAAAD4QAALbPY3VydgAAAAAAAAAaAA"
				+ "AAywHJA2MFkghrC/YQPxVRGzQh8SmQMhg7kkYFUXdd7WtwegWJsZp8rGm/fdPD6TD////bAEMABAMDBAMDBAQDBAUEBAUGCg"
				+ "cGBgYGDQkKCAoPDRAQDw0PDhETGBQREhcSDg8VHBUXGRkbGxsQFB0fHRofGBobGv/bAEMBBAUFBgUGDAcHDBoRDxEaGhoaGho"
				+ "aGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGv/AABEIABAAGAMBIgACEQEDEQH/xAAXAAADAQAAA"
				+ "AAAAAAAAAAAAAAABQYE/8QAIxAAAQQBBAIDAQAAAAAAAAAAAQIDBAURAAYSISIxExQyQf/EABUBAQEAAAAAAAAAAAAAAAAAAAQF"
				+ "/8QAHBEAAQQDAQAAAAAAAAAAAAAAAQACAxEhIjFB/9oADAMBAAIRAxEAPwBZs2ZXT9zRlv3Ehqgg2EiKHniXHWWopBwhIJOCVto"
				+ "Tjx5L/mttls21jb2iBf2a+ljvmxlMSiyqTwcdUsBQS4S2pOfJseSfL9dAxldtKRDFS1XwJzD0KDDZV9cgKEl8uSZAPfpKpEdtRy"
				+ "cfAfeNNqSg3dWyotmqmfbR95UlyaJsdKilx1aclH6CiQodk4KST7A1NsS1i05ugOfFRXztVA3BDRfulD9q+ioYitKSRDY4/I22e"
				+ "WCC5wClEjtXFPpGjU5Mq4Naq6kWlJZWjoZXKCi6264pxl48ChR6+XhKdUD1lKOsEDRp7ZKHEBzLPV//2Q==";
		
		byte [] dataPic;
		BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imageStr)));
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
