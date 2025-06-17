import { Link, useNavigate } from "react-router-dom"
import '../css/Login.css'
import axios from "axios";
import { useState } from "react";

const Login = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    id: "",
    pw: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // 로그인 요청
  const submit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('/api/users/login', {
        id: formData.id,
        pw: formData.pw
      });

      if (response.data === "로그인 성공") {
        console.log(response.data)
        localStorage.setItem("userId", formData.id);
        navigate('/mainrecomdprod');
      } else {
        console.log(response.data)
        alert('아이디 또는 비밀번호가 일치하지 않습니다.');
      }
    } catch (error) {
      console.log("로그인 에러 :", error);
      alert("로그인 중 오류가 발생하였습니다.")
    }
  };

  return (
    <div className="loginFrame">
      <div>

        <div className="bannerBox" />
        <img src="/imgs/bannerImg.png" alt="배너이미지" className="bannerImg" />

        <div className="title">SERENDY</div>
        <div className="slogan">패션 추천 사이트, 슬로건 작성</div>

        <form onSubmit={submit} className="loginFormBox">

          <div>로그인</div>

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

          <button type='submit'>로그인</button>

        </form>

        <Link to='/join'>회원가입</Link>

      </div>
    </div>
  )
}

export default Login