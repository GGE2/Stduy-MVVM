## Model
DTO</br>

DAO
- crud 및 전체 구독자들을 LiveData 형태로 반환</br>

Database
- dao에 대한 인스턴스를 가지고 있음</br>
- 싱글톤으로 구현하기 위해 @Volatile Annotaiton 활용
- @Volatile = PC의 각 코어에는 레지스터 및 캐시가 존재하고 메모리를 각 코어에서 공유하는 구조이다 . 그렇기에 각 코어 별로 변수가 레지스터에 패치되어 존재하면 싱글톤에서 인스턴스가 하나만 생성되는 것 을 보장하지 
못할수도 있다.</br>그렇기에 @Volatile은 이러한 문제를 방지하기 위해 해당 데이터가 메모리에만 존재하도록 명시!!!</br>단 하나의 인스터스를 보장하지만 성능은 하락할 수 있음을 기억하자!!
## Repository
- ViewModel과 잘 상호작용하기 위해 잘 정리된 데이터 API를 들고있음
- 뷰모델은 DB나 서버에 직접 접근하지 않고 Repository에 접근하는 것으로 앱 데이터를 관리

## ViewModel
- Viewmodel 생성시 Repository를 생성자로 받기위해 ViewModelFactory Class 생성
- Event 생성 LiveData를 선언하여 이벤트 발생시 대응 , MVVM 구조를 유지하기 위해 ViewModel이 직접 UI에 관여하지 않음
- viewModelScope안에서 repository method(background thread)를 호출하며 UI 변환시 withContext를 사용하여 Main Thread로 작업

## DataBinding
- Two-way Databinding 
