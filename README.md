# bluesky-boot-autoconfigure-devcheck

<!-- 개발 검증을 위해 사용하는 컨트롤러를 목록화 하여 보여주는 라이브러리입니다. -->
This is a library to list and view the controllers for development confirmation.
A library to list and view controllers for development verification.

## useage

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

Specify the packages to scan as follows.

```properties
bluesky-boot.dev-check.base-packages=net.luversof
```

By default, this library is enabled.

If you want to disable it in a non-development environment, set it as follows.

```properties
bluesky-boot.dev-check.enabled=false
```

### controller

<!-- 
'*DevCheckController' 접미사와 'application/json' produce가 있는 모든 컨트롤러를 나열하고 표시합니다.
다음과 같이 controller를 생성하고 확인하길 원하는 @GetMapping method를 만듭니다.
 -->
List and display all controllers with '*DevCheckController' suffix and 'application/json' produce.
Create a controller and create a @GetMapping method that you want to check like this:

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

<!-- 아래와 같이 '/_check' 접근시 생성한 controller의 getMapping method가 목록에 추가됩니다. -->
As shown below, when '/_check' is accessed, the getMapping method of the created controller is added to the list.

![_check](./_check.png)


### annotation

#### DevCheckDescription

<!-- _check page에서 해당 method에 대한 설명을 나타내기 위해 DevCheckDescription annotation을 사용합니다. -->
DevCheckDescription annotation is used to indicate the description of the method in the _check page.

