import { useState } from 'react';
import '../css/Join.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Join = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    id: "",
    pw: "",
    checkPw: "",
    name: "",
    gender: "",
    ageGroup: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // 회원가입 요청
  const submit = async (e) => {
    e.preventDefault();

    if (formData.pw !== formData.checkPw) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      const response = await axios.post('/api/users/register', {
        id: formData.id,
        pw: formData.pw,
        name: formData.name,
        gender: formData.gender,
        ageGroup: formData.ageGroup
      });

      if (response.data === "회원가입 성공") {
        console.log(response.data)
        alert('회원가입이 완료되었습니다.');
        localStorage.setItem("tempId", formData.id);
        navigate('/stylechoose');
      } else {
        console.log(response.data)
        alert('중복된 아이디입니다. 다른 아이디를 사용해주세요.');
      }
    } catch (error) {
      console.log("회원가입 에러 :", error);
      alert("회원가입 중 오류가 발생하였습니다.")
    }
  };

  return (
    <div className="joinFrame">
      <div>

        <div className="bannerBox" />
        <img src="/imgs/bannerImg.png" alt='배너이미지' className='bannerImg' />

        <div className="title">SERENDY</div>
        <div className="slogan">패션 추천 사이트, 슬로건 작성</div>

        <form onSubmit={submit} className="joinFormBox">

          <div className='joinFormTxt'>회원가입</div>

          <input
            type="text"
            placeholder="이름"
            name='name'
            value={formData.name}
            onChange={handleChange}
            required
          />

          <input
            type="text"
            placeholder="아이디"
            name='id'
            value={formData.id}
            onChange={handleChange}
            required
          />

          <input
            type="password"
            placeholder="비밀번호"
            name='pw'
            value={formData.pw}
            onChange={handleChange}
            required
          />

          <input
            type="password"
            placeholder="비밀번호 확인"
            name='checkPw'
            value={formData.checkPw}
            onChange={handleChange}
            required
          />

          <select
            name="ageGroup"
            value={formData.ageGroup}
            onChange={handleChange}
            required
          >
            <option value="" disabled>연령대 선택</option>
            <option value="20">20대</option>
            <option value="30">30대</option>
            <option value="40">40대</option>
            <option value="50">50대</option>
          </select>

          <div className='joinGender'>
            <label>성별</label>

            <label>
              <input
                type="radio"
                name='gender'
                value='F'
                checked={formData.gender === 'F'}
                onChange={handleChange}
                required
              />
              여성
            </label>

            <label>
              <input
                type="radio"
                name='gender'
                value='M'
                checked={formData.gender === 'M'}
                onChange={handleChange}
              />
              남성
            </label>

          </div>

          <button type='submit'>회원가입</button>

        </form>

      </div>
    </div>
  )
}

export default Join