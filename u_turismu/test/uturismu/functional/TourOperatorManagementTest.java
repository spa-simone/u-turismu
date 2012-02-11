package uturismu.functional;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.is;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.type.SetType;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import uturismu.HashUtil;
import uturismu.ServiceFactory;
import uturismu.dao.AccountDao;
import uturismu.dao.HolidayPackageDao;
import uturismu.dto.Account;
import uturismu.dto.City;
import uturismu.dto.HolidayPackage;
import uturismu.dto.OvernightStay;
import uturismu.dto.Service;
import uturismu.dto.TourOperator;
import uturismu.dto.Transport;
import uturismu.dto.enumtype.AccountType;
import uturismu.dto.enumtype.Status;
import uturismu.exception.ExceptionMessages;
import uturismu.exception.InvalidCredentialException;
import uturismu.service.TourOperatorManagementService;
import uturismu.service.backup.HolidayPackageService;
import uturismu.service.backup.TourOperatorService;

public class TourOperatorManagementTest {

	private static TourOperatorManagementService touroperatorService;
	private static HolidayPackageService serviceHolidayPackage;
	private static City city;
	
	@BeforeClass
	public static void init(){
		city = new City();
		city.setName("Cosenza");
		city.setProvince("Cosenza");
		ServiceFactory.getCityService().save(city);
		touroperatorService=ServiceFactory.getTourOperatorManagementService();
		serviceHolidayPackage=ServiceFactory.getHolidayPackageService();
	}
	
	@Test
	@Ignore
	public void createTourOperatorAccount(){
		String email="tourop@gmail.com";
		String password="password";
		String vatNumber="11111111110";
		Account account=createAccount(email, password);
		TourOperator tourOperator=createTourOperator(account,vatNumber,"UNO");
		Long id = touroperatorService.createAccount(account, tourOperator);
		
		account=null;
		account = touroperatorService.login(email, password);
		assertThat(account, is(org.hamcrest.Matchers.notNullValue()));
		System.out.println(account.getEmail());			
		assertThat(id, is(equalTo(account.getId())));
	}
	
	@Test(expected = InvalidCredentialException.class)
	@Ignore
	public void createTourOperatorWithException(){
		String email="touroperatorException@gmail.com";
		String password="password";
		String vatNumber="12345678901";
		Account account=createAccount(email, password);
		TourOperator tourOperator=createTourOperator(account,vatNumber,"UNO");
		Long id = touroperatorService.createAccount(account, tourOperator);
		
		account=null;
		password="passwordSbagliata";
		account = touroperatorService.login(email, password);
	}
	
	@Test
	@Ignore
	public void queryHolidayPackageTest(){
		String email="tourop@gmail.com";
		String password="password";
		String vatNumber="22222222220";
		Account account1=createAccount(email, password);
		TourOperator TOP1=createTourOperator(account1,vatNumber,"UNO");
		
		TOP1.addHolidayPackage(createHolidayPackage(Status.DRAFT, "Package1", TOP1));
		TOP1.addHolidayPackage(createHolidayPackage(Status.EXPIRED, "Package2", TOP1));
		TOP1.addHolidayPackage(createHolidayPackage(Status.PUBLISHED, "Package3", TOP1));
		
		Long idTOP1=touroperatorService.createAccount(account1, TOP1);
		
		String email2="tourOperatorDUE@gmail.com";
		String password2="passwordDUE";
		String vatNumber2="01333333333";
		Account account2=createAccount(email2, password2);
		TourOperator TOP2=createTourOperator(account2,vatNumber2,"DUE");
		
		TOP2.addHolidayPackage(createHolidayPackage(Status.DRAFT, "Package2_1", TOP2));
		TOP2.addHolidayPackage(createHolidayPackage(Status.PUBLISHED, "Package2_2", TOP2));
		TOP2.addHolidayPackage(createHolidayPackage(Status.PUBLISHED, "Package2_3", TOP2));
		
		Long idTOP2=touroperatorService.createAccount(account2, TOP2);
		
		List<HolidayPackage> list1=touroperatorService.findAllHolidayPackages(idTOP1);
		assertThat(list1, org.hamcrest.Matchers.notNullValue());
		assertThat(list1.size(), is(equalTo(3)));
		
		for (HolidayPackage holidayPackage : list1) {
			assertThat(holidayPackage.getTourOperator().getId(), is(equalTo(TOP1.getId())));
			System.out.println(holidayPackage.getStatus());
		}
		
		list1=touroperatorService.findDraftHolidayPackages(idTOP1);
		for (HolidayPackage holidayPackage : list1) {
			assertThat(holidayPackage.getStatus(), is(equalTo(Status.DRAFT)));
		}
		
		list1=touroperatorService.findExpiredHolidayPackages(idTOP1);
		for (HolidayPackage holidayPackage : list1) {
			assertThat(holidayPackage.getStatus(), is(equalTo(Status.EXPIRED)));
		}
		
		list1=touroperatorService.findPublishedHolidayPackages(idTOP2);
		for (HolidayPackage holidayPackage : list1) {
			assertThat(holidayPackage.getStatus(), is(equalTo(Status.PUBLISHED)));
		}
	}
	
	@Test
	@Ignore
	public void addServices(){
		Service serviceONE = new OvernightStay();
		Service serviceTWO = new Transport();
		serviceONE.setId(1L);
		serviceONE.setPrice(35d);
		serviceONE.setDescription("SERVIZIO ALBERGHIERO");
		
		serviceTWO.setId(2L);
		serviceTWO.setPrice(500.0);
		serviceTWO.setDescription("SERVIZIO DI TRASPORTO FERROVIARIO");
		
		String email="TESTING_ADDSERVICES@gmail.com";
		String password="password";
		String vatNumber="33333";
		Account account1=createAccount(email, password);
		TourOperator TOP1=createTourOperator(account1,vatNumber,email);
		
		HolidayPackage hp1= createHolidayPackage(Status.DRAFT,"Pack_0", TOP1);
//		hp1.setId(1L);
		HolidayPackage hp2=createHolidayPackage(Status.DRAFT, "Package1", TOP1);
//		hp2.setId(2L);
		HolidayPackage hp3=createHolidayPackage(Status.DRAFT, "Package2", TOP1);
//		hp3.setId(3L);
		HolidayPackage hp4=createHolidayPackage(Status.PUBLISHED, "Package3", TOP1);
//		hp4.setId(4L);
		
		hp1.addService(serviceONE);
		
		TOP1.addHolidayPackage(hp1);
		TOP1.addHolidayPackage(hp2);
		TOP1.addHolidayPackage(hp3);
		TOP1.addHolidayPackage(hp4);

//		TOP1.setId(11L);
		Long id=touroperatorService.createAccount(account1, TOP1);
		
		List<HolidayPackage> list=touroperatorService.findDraftHolidayPackages(id);
		assertThat(list.size(), is(equalTo(3)));
		for (HolidayPackage holidayPackage : list) {
			System.out.println(holidayPackage.getStatus());
		}
		
		HolidayPackage hpGet=list.get(0);
		
		hpGet.addService(serviceTWO);
//		ServiceFactory.getHolidayPackageService().update(hpGet);
		touroperatorService.updateHolidayPackage(hpGet);
		
//		HolidayPackage hpTest=ServiceFactory.getHolidayPackageService().findById(hpGet.getId());
		assertThat(hp1.hashCode(),is(equalTo(hpGet.hashCode())));
		assertThat(hpGet.getTourOperator(),is(equalTo(TOP1)));		
		
		for ( Service servizio : hpGet.getServices()) {
			System.out.println(hpGet.getName() + "  "+servizio.getDescription()+ " prezzo "+servizio.getPrice());
		}
		
	}
	
	@Test
	public void updateHolidayPackage(){
		String email="TESTING_UPDATE@gmail.com";
		String password="password";
		String vatNumber="98765432122";
		Account account1=createAccount(email, password);

		TourOperator TOP1=createTourOperator(account1,vatNumber,"UNO");
		
		HolidayPackage PAck1=createHolidayPackage(Status.DRAFT, "Package1", TOP1);
		HolidayPackage PAck2=createHolidayPackage(Status.DRAFT, "Package2", TOP1);
		HolidayPackage PAck3=createHolidayPackage(Status.PUBLISHED, "Package3", TOP1);
		
		TOP1.addHolidayPackage(PAck1);
		TOP1.addHolidayPackage(PAck2);
		TOP1.addHolidayPackage(PAck3);

		Long ID=touroperatorService.createAccount(account1, TOP1);
		
		List<HolidayPackage> list= touroperatorService.findDraftHolidayPackages(ID);		
		assertThat(list.size(), is(equalTo(2)));
		
		HolidayPackage hpToUpdate=list.get(0);
		
		System.out.println(PAck1.getId()+ " "+PAck1.getName()+" "+PAck1.getTourOperator().getName());
		System.out.println(hpToUpdate.getId()+ " "+hpToUpdate.getName()+" "+hpToUpdate.getTourOperator().getName());
		
		String description="setto la descrizione di update ";
		hpToUpdate.setDescription(description);
		hpToUpdate.setAvailability(10);
		hpToUpdate.setStatus(Status.PUBLISHED);
		touroperatorService.updateHolidayPackage(hpToUpdate);
		
		
//		HolidayPackage HP_test=ServiceFactory.getHolidayPackageService().findById(hpToUpdate.getId());
		
		
		HolidayPackage HP_test=touroperatorService.hpQueryID(1L);
		System.out.println(HP_test.getId()+ " "+HP_test.getName()+" "+HP_test.getTourOperator()+" "+HP_test.getDescription()+" "+HP_test.getAvailability());
		
		
	}
	
	private HolidayPackage createHolidayPackage(Status status,String Name, TourOperator top){
		HolidayPackage hp=new HolidayPackage();
		hp.setAvailability(3);
		hp.setCustomerNumber(2);
		hp.setStatus(status);
		hp.setDueDate(new Date());
		hp.setName("pacchettovacanze_"+status+Name);
		return hp;
	}
	
	private TourOperator createTourOperator(Account account,String vatNumber,String name){
		TourOperator to=new TourOperator();
		to.setHolderName("pippo Inzaghi");
		to.setName("Company "+name);
		to.setVatNumber(vatNumber);
		to.setAccount(account);
		account.setTourOperator(to);
		return to;
	}
	
	private TourOperator createTourOperatorEX(Account account){
		TourOperator to=new TourOperator();
		to.setHolderName("Eccezzione");
		to.setName("io Sono un eccezzione");
		to.setVatNumber("11111111111");
		to.setAccount(account);
		account.setTourOperator(to);
		return to;
	}
	
	private Account createAccount(String email,String password){
		Account account=new Account();
		account.setActive(true);
		account.setEmail(email);
		String salt=HashUtil.generateSalt();
		account.setSalt(salt);
		String psw=HashUtil.getHash(password, salt);
		account.setPassword(psw);
		account.setType(AccountType.TOUR_OPERATOR);
		account.setLastAccessTimestamp(new Date());
		account.setRegistrationTimestamp(new Date());
		return account;
	}
	
	
	
}






