# allra-market


### 수정사항
- 도커 올리기: mriadb 로컬설치 이슈로 H2 mariadb 모드 사용
- 결제 통합 테스트 방법 - CRUD
- (고민중) 화면, Security


### 사용기술 
java17, spring boot3.4, JPA, H2_mariadb 

### 관점
- 학습중인 DDD의 레이어드 아키텍처를 최대한 적용하고자 노력
- 구현 위주가 아닌, 의미있는 메서드를 작성하고자 노력
- Mock, Assertion 등 기능을 활용해 테스트 케이스를 최대한 작성하고자 노력
- 응집도를 높이고, 결합도를 낮출 수 있는 방향으로 코드배치를 고민

### 주요 작성 도메인(스키마)
- Customer(고객)
- Product(상품)
- Order(주문)
- OrderItem(주문상품)
- Cart(장바구니)
- CartItem(장바구니상품)
- Payment(결제)

### 주요 기능
- Customer는 생성시 장바구니(Cart)를 세팅하며, 장바구니 내부의 CartItem 통해 Product를 임시 저장합니다.
- 주문 요청시, 장바구니 total price 가져와 결제하며, Order-OrderItem을 통해 이력을 관리합니다.
- OrderPayment 서비스에서는 결제(Payment) 후 기타 도메인들이 로직을 처리하도록 서비스 클래스를 분리 작성하였습니다.
  - Payment: 완료,실패 state 변경, 인스턴스 저장
  - Product: 성공시 수량 감소
  - Order: 주문 상태 변경
  - Cart: 구매한 항목(CartItem) 장바구니 삭제
 
### 기타 포인트
- 원활한 테스트를 위해 DataInit 데이터를 통해 **어플리케이션이 올라가면서 일부 초기 테스트 데이터가 세팅**됩니다.
- 특징있는 Exception들을 커스텀하게 작성하였으며, RestControllerAdvice를 활용해 처리했습니다.
- @Valid 어노테이션을 활용하여 request dto의 필수값, 최소값 등을 초기에 잡도록 작성했습니다.
- record 클래스로 dto를 작성하여 불변성을 최대한 활용했습니다.

  
### [결제 API 테스트]

#### method: 
- POST
#### endpoint: 
- http://localhost:8080/api/order/payment

#### request body:
{
    "orderId":1,
    "price":1000,
    "paymentMethod": "CASH"
}
