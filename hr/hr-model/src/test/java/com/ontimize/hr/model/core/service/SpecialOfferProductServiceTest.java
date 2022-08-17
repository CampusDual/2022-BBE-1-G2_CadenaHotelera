package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.hr.model.core.dao.SpecialOfferProductDao;
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.entities.OfferProduct;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class SpecialOfferProductServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private EntityUtils entityUtils;

	@Mock
	private CredentialUtils credentialUtils;

	@Mock
	private SpecialOfferProductDao specialOfferProductDao;

	@Spy
	@InjectMocks
	private SpecialOfferProductService service;

	@Test
	@DisplayName("Fail Add Product No data")
	void productAddEmptyMap() {
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean()))
				.thenThrow(new FillException(MsgLabels.PRODUCT_EMPTY));
		EntityResult res = service.specialOfferProductAdd(new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_EMPTY, res.getMessage());
	}

	@Test
	@DisplayName("Fail Add Invalid Product")
	void productAddInvalid() {
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct());
		doReturn(MsgLabels.DETAILS_TYPE_ID_MANDATORY).when(service).isProductValid(isA(OfferProduct.class));
		EntityResult res = service.specialOfferProductAdd(new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fail Add, Global offer local User")
	void productAddLocalUserGlobalOffer() {
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct() {
			{
				setSpecialOfferId(1);
				setDetId(1);
			}
		});
		doReturn(null).when(service).isProductValid(isA(OfferProduct.class));
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(false);
		EntityResult res = service.specialOfferProductAdd(new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER, res.getMessage());
	}

	@Test
	@DisplayName("Fail Add, Duplicated Product")
	void productAddDuplicatedProduct() {
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct() {
			{
				setSpecialOfferId(1);
				setDetId(1);
			}
		});
		doReturn(null).when(service).isProductValid(isA(OfferProduct.class));
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(true);
		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>() {
							{
								put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
								put(SpecialOfferProductDao.ATTR_DET_ID, 1);
							}
						});
					}
				});
		EntityResult res = service.specialOfferProductAdd(new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_DUPLICATED_PRODUCT, res.getMessage());
	}

	@Test
	@DisplayName("Fake Add")
	void productFakeAdd() {
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct() {
			{
				setSpecialOfferId(1);
				setDetId(1);
			}
		});
		doReturn(null).when(service).isProductValid(isA(OfferProduct.class));
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(true);
		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl());
		when(daoHelper.insert(isA(SpecialOfferProductDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferProductAdd(new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());

	}

	@Test
	@DisplayName("product modify filter with no det id")
	void productModifyNoDetId() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct());
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter with no offer id")
	void productModifyNoSpecialOfferId() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
					}
				});
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_ID_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter error Fetching")
	void productModifyErrorFetching() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		keyMap.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
						setSpecialOfferId(1);
					}
				});

		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						setCode(OPERATION_WRONG);
					}
				});
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR_FETCHING_BASE_PRODUCT, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter details type not exists")
	void productModifyDetailsTypeNotExists() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		keyMap.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
						setSpecialOfferId(1);
					}
				});

		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl());
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(false);
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_NOT_EXISTS, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter details type not exists")
	void productModifySpecialOfferNotExists() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		keyMap.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
						setSpecialOfferId(1);
					}
				});

		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl());
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(false);
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter baseProduct not exists")
	void productModifyBaseProductNotExists() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		keyMap.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
						setSpecialOfferId(1);
					}
				});

		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl());
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_NOT_EXISTS, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter baseProduct not exists")
	void productModify() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		keyMap.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
						setSpecialOfferId(1);
					}
				}, new OfferProduct() {
					{

					}
				});

		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>() {
							{
								put(SpecialOfferProductDao.ATTR_DET_ID,1);
								put(SpecialOfferProductDao.ATTR_OFFER_ID,1);
								put(SpecialOfferProductDao.ATTR_PERCENT,30.0);
							}
						});
					}
				});
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.mergeProducts(isA(OfferProduct.class), isA(OfferProduct.class))).thenReturn(new OfferProduct());
		doReturn(MsgLabels.PRODUCT_EMPTY).when(service).isProductValid(isA(OfferProduct.class));
		
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_EMPTY, res.getMessage());
	}

	@Test
	@DisplayName("product modify filter baseProduct not exists")
	void productModify132() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		keyMap.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		when(entityUtils.fillProduct(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferProduct(),
				new OfferProduct() {
					{
						setDetId(1);
						setSpecialOfferId(1);
					}
				}, new OfferProduct() {
					{

					}
				});

		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl());
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		EntityResult res = service.specialOfferProductModify(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_NOT_EXISTS, res.getMessage());
	}

}
