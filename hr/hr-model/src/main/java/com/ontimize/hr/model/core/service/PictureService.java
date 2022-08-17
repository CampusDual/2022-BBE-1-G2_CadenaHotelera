package com.ontimize.hr.model.core.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ontimize.hr.api.core.service.IPictureService;
import com.ontimize.hr.model.core.dao.PictureDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.util.remote.BytesBlock;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class PictureService.
 */
@Service("PictureService")
@Lazy
public class PictureService implements IPictureService {

	private static final Logger LOG = LoggerFactory.getLogger(PictureService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private PictureDao pictureDao;

	/**
	 * PictureQuery.
	 *
	 * @param keyMap   the key map in which you enter the conditions of the query
	 * @param attrList the attr list where you enter the columns to get
	 * @return the entity result come the results of the query
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult pictureQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.pictureDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	/**
	 * PictureInsert.
	 *
	 * @param attrMap the attr Map where you enter the data to insert
	 * @return the entity result comes an ok and the id of the created element
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult pictureInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		byte [] data;
		try {
			BufferedImage bImage = ImageIO.read(new File(attrMap.get("pic").toString()));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bImage, "jpg", bos );
			data = bos.toByteArray();
			
			attrMap.remove("pic");
			attrMap.put("pic", data);
		}catch(Exception e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}		
		
		try {
			return this.daoHelper.insert(this.pictureDao, attrMap);
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.ROOM_TYPE_NAME_DUPLICATED);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ROOM_TYPE_NAME_DUPLICATED);
			return res;
		} catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.ROOM_TYPE_NAME_BLANK);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ROOM_TYPE_NAME_BLANK);
			return res;
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	/**
	 * PictureUpdate.
	 *
	 * @param attrMap the attr Map where you enter the data to update
	 * @param keyMap  the key map in which you enter the conditions of the query
	 * @return the entity result comes an ok
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult pictureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)throws OntimizeJEERuntimeException {
		
		if(attrMap.containsKey(PictureDao.ATTR_PICTURE)) {
			byte [] data;
			try {
				BufferedImage bImage = ImageIO.read(new File(attrMap.get("pic").toString()));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(bImage, "jpg", bos );
				data = bos.toByteArray();
				
				attrMap.remove("pic");
				attrMap.put("pic", data);
			}catch(Exception e) {
				LOG.info(MsgLabels.BAD_DATA);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
			}
		}		
		
		try {
			return this.daoHelper.update(this.pictureDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.PICTURE_NAME_BLANK);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.PICTURE_NAME_BLANK);
			return res;
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	/**
	 * PictureDelete.
	 *
	 * @param keyMap the key map in which you enter the conditions of the query
	 * @return the entity result comes an ok
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult pictureDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		try {
			if (this.daoHelper.query(pictureDao, keyMap, Arrays.asList(PictureDao.ATTR_ID))
					.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.NO_DATA_TO_DELETE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
			}
			return this.daoHelper.delete(this.pictureDao, keyMap);
		}  catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}

	}
	
	/**
	 * getPicture. Obtiene una foto almacenada en BD y la convierte a .jpg
	 *
	 * @param req contains the picture id
	 * @return a photo
	 */
	@Secured({ PermissionsProviderSecured.SECURED })
	public ResponseEntity getPicture(Map<String,Object> req) {
		
		if(!req.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new ResponseEntity(MsgLabels.DATA_MANDATORY,HttpStatus.BAD_REQUEST);
		}
		Map<String,Object> data = (Map<String, Object>) req.get("data");
		
		Integer idPicture;
		if(!data.containsKey(PictureDao.ATTR_ID)) {
			LOG.info(MsgLabels.PICTURE_ID_MANDATORY);
			return new ResponseEntity(MsgLabels.PICTURE_ID_MANDATORY,HttpStatus.BAD_REQUEST);
		}else {
			try {
				idPicture=Integer.parseInt(data.get(PictureDao.ATTR_ID).toString());
			} catch(NumberFormatException e){
				LOG.info(MsgLabels.PICTURE_ID_FORMAT);
				return new ResponseEntity(MsgLabels.PICTURE_ID_FORMAT,HttpStatus.BAD_REQUEST);
			}
		}
		
		//hasta aqui, comprobaciones.
		
		List<String> list = Arrays.asList(PictureDao.ATTR_PICTURE);
		Map<String,Object> map = new HashMap<>();
		map.put(PictureDao.ATTR_ID, idPicture);
		
		EntityResult pictureEntity = this.daoHelper.query(pictureDao, map, list);
		
		//consultamos la BD para obtener la foto.
		
		if(pictureEntity.calculateRecordNumber()==0) {
			LOG.info(MsgLabels.PICTURE_NOT_EXISTS);
			return new ResponseEntity(MsgLabels.PICTURE_NOT_EXISTS,HttpStatus.BAD_REQUEST);
		}		
		
		Object varPic = pictureEntity.getRecordValues(0).get(PictureDao.ATTR_PICTURE);
		byte[] yourBytes = ((BytesBlock)varPic).getBytes();
		
		//almacenamos la foto en un array de bytes.
		
		HttpHeaders headers = new HttpHeaders();
		byte[] contents = null;
		
		contents = yourBytes;
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		String filename = "img.jpg";
		headers.setContentDispositionFormData(filename,filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");		
		
		return new ResponseEntity(contents,headers,HttpStatus.OK);
	}
	
	/**
	 * postPicture. envia una foto jpg al programa y este la almacena en un array de bits en BD
	 * 
	 * @param mf contains the picture
	 * @param s the name of the picture
	 * @return ok and the picture id in the BD
	 */
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult postPicture(MultipartFile mf, String s, String c) {
		
		if(mf.isEmpty()) {
			LOG.info(MsgLabels.PICTURE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.PICTURE_MANDATORY);
		}
		
		if(!mf.getContentType().equals("image/jpeg")) {
			LOG.info(MsgLabels.PICTURE_WRONG_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.PICTURE_WRONG_FORMAT);
		}
		
		
		if(s.isBlank()||s.isEmpty()) {
			LOG.info(MsgLabels.PICTURE_NAME_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.PICTURE_NAME_MANDATORY);
		}
		
			
		byte[] yourBytes=null;
		try {
			 yourBytes = mf.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//obtenemos el array de bytes a partir de la foto
		Map<String,Object> attrMap = new HashMap<>();
		attrMap.put(PictureDao.ATTR_NAME, s);
		attrMap.put(PictureDao.ATTR_PICTURE, yourBytes);
		
		if(!c.isEmpty()&&!c.isBlank()) {
			attrMap.put(PictureDao.ATTR_DESCRIPTION, c);
		}

		return daoHelper.insert(pictureDao, attrMap);
	}
	
	/**
	 * getPictureArray. obtiene el valor en bytes de una foto
	 * 
	 * @param req contains the picture id
	 * @return ok and the picture id in the BD
	 */
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult getPictureArray(Map<String,Object> req) {
		if(!req.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.DATA_MANDATORY);
		}
		Map<String,Object> data = (Map<String, Object>) req.get("data");
		
		Integer idPicture;
		if(!data.containsKey(PictureDao.ATTR_ID)) {
			LOG.info(MsgLabels.PICTURE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.PICTURE_ID_MANDATORY);
		}else {
			try {
				idPicture=Integer.parseInt(data.get(PictureDao.ATTR_ID).toString());
			} catch(NumberFormatException e){
				LOG.info(MsgLabels.PICTURE_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.PICTURE_ID_FORMAT);
			}
		}
		
		//comprobaciones
		
		List<String> list = Arrays.asList(PictureDao.ATTR_ID,PictureDao.ATTR_PICTURE,PictureDao.ATTR_DESCRIPTION);
		Map<String,Object> map = new HashMap<>();
		map.put(PictureDao.ATTR_ID, idPicture);
		
		EntityResult pictureEntity = daoHelper.query(pictureDao, map, list);
		
		//obtenemos la foto por su id
		
		if(pictureEntity.calculateRecordNumber()==0) {
			LOG.info(MsgLabels.PICTURE_NOT_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.PICTURE_NOT_EXISTS);
		}
		
			
		Object varPic = pictureEntity.getRecordValues(0).get(PictureDao.ATTR_PICTURE);
		byte[] yourBytes = ((BytesBlock)varPic).getBytes();
				
		byte[] contents = null;
		contents = yourBytes;
		
		EntityResult res = new EntityResultMapImpl();
		Map<String,Object> foto = new HashMap<>();
		foto.put(PictureDao.ATTR_PICTURE, contents);
		foto.put(PictureDao.ATTR_DESCRIPTION, pictureEntity.getRecordValues(0).get(PictureDao.ATTR_DESCRIPTION));
		foto.put(PictureDao.ATTR_ID, pictureEntity.getRecordValues(0).get(PictureDao.ATTR_ID));
		res.addRecord(foto);
		return res;
	}

}
