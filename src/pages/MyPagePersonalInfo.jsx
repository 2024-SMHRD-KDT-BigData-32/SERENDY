import '../css/MyPagePersonalInfo.css'

const MyPage = () => {
  return (
    <div className="myPageFrame">
        <div>

            <div className="bannerBox"/>
            <img src="/imgs/배너이미지1.png" className='bannerImg'/>

            <div className="title">SERENDY</div>
            <div className="slogan">패션 추천 사이트, 슬로건 작성</div>

            <div className="myPageFormBox">

              <div className='myPageFormTxt'>마이페이지</div>
 
              <input
                type="text"
                placeholder="이름"
              />
              
              <input
                type="text"
                placeholder="아이디"
              />
              
              <input
                type="password"
                placeholder="비밀번호"
              />

              <input
                type="password"
                placeholder="비밀번호 확인"
              />

              <select name="" id="">
                <option disabled selected>연령대 선택</option>
                <option value="">20대</option>
                <option value="">30대</option>
                <option value="">40대</option>
                <option value="">50대</option>
              </select>

              <div className='myPageGender'>
                <label>성별</label>

                <label>
                  <input
                      type="radio"
                      name='gender'
                      value='W'
                  />
                여성
                </label>

                <label>
                  <input
                      type="radio"
                      name='gender'
                      value='M'
                  />
                  남성
                </label>

              </div>

              <button className='modifyBtn'>수정</button>

            </div>

              <button className='delAccBtn'>회원탈퇴</button>

        </div>
    </div>
  )
}

export default MyPage