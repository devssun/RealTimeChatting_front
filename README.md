# RealTimeChatting
Android Mentoring Real Time Chatting

기초 설명

<리스너>
1. 콜백 메소드 재정의 - 실행할 때마다 상속을 받음 => 비효율성
2. 리스너 인터페이스 구현 
* 인터페이스 -> 가이드역할, 안에 함수는 반드시 써야함
이벤트에 대한 리스너클래스를 만들고 객체 생성 리스너에 객체를 보내준다
3. 액티비티가 리스너구현
인터페이스가 액티비티에 연결
4. this -> 자신 클래스에 대한 객체
5. 익명 내부클래스사용

onTouchListener -> 인터페이스
* onTouch : 추상 메소드 -> 재정의(Override)가 필요

<ListView>
ListView->Adapter->data
* CharSequnce : XML에서 가져오는 String array
* String : Java에서 가져오는 String array(주로 값이 바뀔 때 사용한다)
* Adapter에다 값을 넣어서 가져왕!  리스트뷰는 그냥 보여주는 역할.


<Inflater>
* Inflater : 전개, 펼친다
* XML파일을 가져올 때 사용한다
* ex) mInflater.inflate(R.menu.menu_main, menu);
* menu XML을 가져온다


<Intent>
* 액티비티간에 인수와 리턴값을 전달하는 도구로 사용
* Intent(Context packageContext, Class<?> cls)
* 첫번째 인자는 자기 자신, 두번째 인자는 이동할 클래스
* this -> 자기 자신을 가리킴
* MainActivity.this -> 다른 이벤트에서 갈때


* 명시적 인텐트 : 호출대상 컴포넌트가 분명히 명시되어 있는 것
* 암시적 인텐트 : 호출 대상이 정확히 정해지지 않은 인텐트
* 
* putExtra() - 매개변수 앞의 키로 값을 찾는다


<액티비티 간의 통신>
* getStringExtra : 값을 가져온다
* requestCode : 액티비티가 여러개 있을 경우 분별해주는 역할
* getIntent() : 액티비티로 전달된 인텐트를 전달

<암시적 인텐트>

