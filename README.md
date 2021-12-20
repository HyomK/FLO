## FLO Clone Project
프로젝트에 사용된 서버는 종료되어 API 사용이 불가능합니다.\br
녹화 영상을 통해 실제 적용모습을 확인해 주세요 \br
사용 DB: Room Database

*구현 주요 기능
  *HomeFragment -> 화면 실행 시 가장 처음보이는 화면 
    * 슬라이딩 배너
    * 앨범 이미지 선택 시 앨범 정보 fragment로 이동
    * API를 통해 앨범 정보를 가져와 뿌림
  *MainActiviy -> 각 Fragment 연동에 사용되는 navigation
    * miniplayer 기능으로 현재 실행되고 있는 음악 재생 control
  *SongActiviy
    * 음악 플레이어로 DB에 좋아요를 누른 곡을 담거나 뺌
    * 음악 재생 Contrl
  *LoginActivity
    * API 를 통해 회원 관리
    * 회원 가입 및 로그인 기능
  *LockerFragment
    *DB에 저장된 앨범, 노래들을 recyclerView로 뿌려줌
  *lookFragment
    * API를 통해 FLO차트 정보를 받아 뿌려줌
  * SavedSongFragment  
    *이용자에 따라 좋아요한 곡 테이블 정보를 가져와 보여줌
   
