# 이전 코드와의 차이점

* 기존 `JCF*Service`와 `File*Service`는 비즈니스 로직과 저장 로직을 모두 가지고 있었지만, `Basic*Service`는 비즈니스 로직만 담당하고 저장 로직은 Repository에 넘긴다.
* 기존에는 저장 방식마다 Service를 따로 구현해야 했지만, 현재는 Repository 구현체만 교체하면 같은 `Basic*Service`를 재사용할 수 있다.
* 기존에는 비즈니스 로직이 여러 Service 구현체에 중복되었지만, 현재는 하나의 `Basic*Service`에만 존재한다.
