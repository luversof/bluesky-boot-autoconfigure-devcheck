# bluesky-boot-autoconfigure-devcheck

<!-- 
bluesky-boot-autoconfigure-devcheck는 Spring Boot 기반 개발 환경에서 개발 확인을 위해 사용되는 controller의 method와 utility static method를 목록화 하여 보여주는 라이브러리입니다.
개발 확인용 controller method 목록은 '/_check' 에서 확인할 수 있고 utility static method 목록은 '/_check/util' 에서 확인할 수 있습니다.
-->
bluesky-boot-autoconfigure-devcheck is a library that lists and shows the methods of the controller and utility static methods used to check the development in the Spring Boot-based development environment.
You can check the list of controller methods for development check at '/_check' and the list of utility static methods at '/_check/util'.

**Prerequisites**

- [Java 11](https://openjdk.java.net/)
- [Spring Boot 2.5.3](https://spring.io/)

## settings

### maven dependencies

```pom.xml
<dependencies>
    <dependency>
        <groupId>io.github.luversof</groupId>
        <artifactId>bluesky-boot-autoconfigure-devcheck</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### properties

<!--
지정된 범위에서 utility static method를 검색합니다.
다음과 같이 검사할 패키지를 지정합니다.
-->
Searches for utility static methods in the specified scope.
Specifies the packages to scan as follows:

```properties
bluesky-boot.dev-check.base-packages=net.luversof
```

By default, this library is enabled.

If you want to disable it in a non-development environment, set it as follows.

```properties
bluesky-boot.dev-check.enabled=false
```

## usage

### DevCheckDescription annotation

<!-- 
'/_check' page에서 해당 method에 대한 설명을 나타내기 위해 DevCheckDescription annotation을 사용합니다.
controller method와 utility static method에 사용할 수 있습니다.
-->
DevCheckDescription annotation is used to indicate the description of the method in the '/_check' page.
Can be used for controller methods and utility static methods.

| attribute  | description |
| ------------- | ------------- |
| value  | <!-- method 아래 추가되는 설명 --> Description added under method  |
| displayable  | <!-- 목록 노출 여부 --> Whether the list is exposed  |

### controller method

<!-- 
bean name이 '*DevCheckController' 이고 produce가 'application/json' 인 모든 컨트롤러가 '/_check' page 목록화 대상입니다.
다음과 같이 controller를 생성합니다.
 -->
All controllers with bean name of '*DevCheckController' and produce of 'application/json' are eligible for '/_check' page listing.
Create a controller like this:

```java
@RestController
@RequestMapping(value = "/_check/core",  produces = MediaType.APPLICATION_JSON_VALUE)
public class CoreDevCheckController {

	private ApplicationContext applicationContext;

	public CoreDevCheckController(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@DevCheckDescription("spring activeProfiles")
	@GetMapping("/activeProfiles")
	public String[] activeProfiles() {
		return applicationContext.getEnvironment().getActiveProfiles();
	}
	
	// create GetMapping method you want to check
}
```

<!-- 
아래와 같이 해당 controller의 getMapping method가 '/_check' 목록에 추가됩니다.
-->
The getMapping method of the controller is added to the '/_check' list as shown below.

![_check](./_check.png)


### utility static method

<!--
'/_check/util' 목록에 추가할 utility class에 @DevCheckUtil annotation을 선언합니다.
다음과 같이 사용합니다.
-->
Declare @DevCheckUtil annotation in the utility class to be added to the '/_check/util' list.
Use it like this:

```java
@DevCheckUtil
public abstract class UserUtil extends RequestAttributeUtil {
	
	@Setter(onMethod_ = @DevCheckDescription(displayable = false))
	private static LoginUserService loginUserService;
	
	private static final String LOGIN_USER = "__loginUser";

	@DevCheckDescription("get loginUser")
	public static Optional<User> getLoginUser() {
		Optional<User> userOptional = getRequestAttribute(LOGIN_USER);
		if (userOptional != null) {
			return userOptional;
		}
		
		userOptional = loginUserService.getUser();
		setRequestAttribute(LOGIN_USER, userOptional);
		
		return userOptional;
	}
}

```

<!--
아래와 같이 해당 utility static method가 '/_check/util' 목록에 추가됩니다.
-->
The utility static method is added to the '/_check/util' list as shown below.

![_check](./_checkUtil.png)
