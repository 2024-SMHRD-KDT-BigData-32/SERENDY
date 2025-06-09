import { Link } from "react-router-dom"
import '../css/Login.css'

const Login = () => {
  return (
    <div className="loginFrame">
        <div>

            <div className="bannerBox"/>
            <img src="/imgs/배너이미지1.png" className="bannerImg" />

            <div className="title">SERENDY</div>
            <div className="slogan">패션 추천 사이트, 슬로건 작성</div>

            <div className="loginFormBox">

              <div>로그인</div>

              <input
                type="text"
                placeholder="아이디"
              />
              
              <input
                type="password"
                placeholder="비밀번호"
              />

              <button>로그인</button>

            </div>

            <Link to='/join'>회원가입</Link>

        </div>
    </div>
  )
}

export default Login