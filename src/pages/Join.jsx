import { useState } from 'react';
import '../css/Join.css'

const Join = () => {
  const [checkIdMsg, setCheckIdMsg] = useState('');

  const checkId = () => {
    setCheckIdMsg('테스트 메시지입니다.');
  };

  return (
    <div className="joinFrame">
        <div>

            <div className="bannerBox"/>
            <img src="/imgs/배너이미지1.png" className='bannerImg'/>

            <div className="title">SERENDY</div>
            <div className="slogan">패션 추천 사이트, 슬로건 작성</div>

            <div className="joinFormBox">

              <div className='joinFormTxt'>회원가입</div>

              <input
                type="text"
                placeholder="이름"
              />
              
              <div className="idInputGroup">
                <input
                  type="text"
                  placeholder="아이디"
                  onBlur={checkId}
                />
                <div className="checkIdBox">
                  {checkIdMsg && (
                    <div className="checkIdMsg">{checkIdMsg}</div>
                  )}
                </div>
              </div>
              
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

              <div className='joinGender'>
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

              <button>회원가입</button>

            </div>

        </div>
    </div>
  )
}

export default Join