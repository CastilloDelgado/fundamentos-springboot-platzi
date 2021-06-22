package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithPropertiesImplement;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(this.getClass());

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;

	public FundamentosApplication(@Qualifier("componentImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService){
		this.componentDependency = componentDependency;
		this.myBean= myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		//getInformationJpqlFromUser();
		//saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("test1Transactional", "test1Transactional@emergys.com", LocalDate.now());
		User test2 = new User("test2Transactional", "test2Transactional@emergys.com", LocalDate.now());
		User test3 = new User("test3Transactional", "test3Transactional@emergys.com", LocalDate.now());
		User test4 = new User("test4Transactional", "test1Transactional@emergys.com", LocalDate.now());

		List<User>  users = Arrays.asList(test1, test2, test3, test4);

		try{
			userService.saveTransactional(users);
		} catch(Exception e){
			LOGGER.error("Esta es una expeción destro del método transactional" + e);
		}

		userService
			.getAllUsers()
			.stream()
			.forEach(user ->
					LOGGER.info("Este es el usuario dentro del método transaccional: " + user.toString()));
	}

	private void getInformationJpqlFromUser(){
		LOGGER.info("Usuario con el método findByUserEmail " +
				userRepository.findByUserEmail("marco.castillo@emergys.com")
						.orElseThrow(()-> new RuntimeException("No se encontró el usuario")));

		userRepository.findAndSort("user", Sort.by("id").ascending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con método sort " + user.getName()));

		userRepository.findByName("Marco")
		.stream()
		.forEach(user -> LOGGER.info("Usuario con query method " + user.toString()));

		LOGGER.info("Usuario con query method findByEmailAndName " +
				userRepository.findByEmailAndName("juan.castillo@emergys.com", "Juan")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%M%")
				.stream()
				.forEach(user -> LOGGER.info(" Usuario con query method findByNameLike, " + user.toString()));

		userRepository.findByNameOrEmail(null, "user3@emergys.com")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findbyNameOrEmail " + user.toString()));

		userRepository
				.findByBirthDateBetween(LocalDate.of(1997, 03, 1), LocalDate.of(1997, 06, 1))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method findByBirthDateBetween " + user.toString()));

		userRepository.findByNameLikeOrderByIdDesc("%u%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method findByNameLikeOrderByIdDesc " + user.toString()));

		LOGGER.info("El usuario es: " + userRepository
				.getAllByBirthDateAndEmail(LocalDate.of(1997,03,29), "marco.castillo@emergys.com")
				.orElseThrow(() -> new RuntimeException("No se encontró el usuario")));
	}

	private void saveUsersInDataBase(){
		User user1 = new User("Marco", "marco.castillo@emergys.com", LocalDate.of(1997,03,29));
		User user2 = new User("Juan", "juan.castillo@emergys.com", LocalDate.of(1997,03,29));
		User user3 = new User("Marco", "user3@emergys.com", LocalDate.of(1997,03,29));
		User user4 = new User("user4", "user4@emergys.com", LocalDate.of(1997,04,29));
		User user5 = new User("user5", "user5@emergys.com", LocalDate.of(1997,05,29));
		User user6 = new User("user6", "user6@emergys.com", LocalDate.of(1997,06,29));
		User user7 = new User("user7", "user7@emergys.com", LocalDate.of(1997,07,29));
		User user8 = new User("user8", "user8@emergys.com", LocalDate.of(1997,01,29));
		User user9 = new User("user9", "user9@emergys.com", LocalDate.of(1997,011,29));
		User user10 = new User("user10", "user10@emergys.com", LocalDate.of(1997,10,29));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10);
		userRepository.saveAll(list);
	}

	private void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword());
		try {
			int value = 10/0;
			LOGGER.debug("Mi valor: " + value);
		} catch(Exception e){
			LOGGER.error("Esto es un error al dividir por cero: " + e.getStackTrace());
		}
	}
}