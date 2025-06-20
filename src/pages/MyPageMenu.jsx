import { Link, useNavigate } from 'react-router-dom'
import '../css/MyPageMenu.css'
import { useState } from 'react';
import axios from 'axios';

const MyPageMenu = () => {
  const [showHelp, setShowHelp] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const navigate = useNavigate()
  const userId = localStorage.getItem('userId')
  const [userStyle, setUserStyle] = useState([]);

  const resetStyle = async () => {
    try {
      const res = await axios.get(`/api/users/getStylePref/${userId}`);
      const styleList = res.data;
      setUserStyle(styleList);
      setShowConfirmModal(true);
    } catch (err) {
      console.error('추천 상품 로딩 실패:', err);
    }
  }

  const handleConfirmReset = () => {
    localStorage.setItem('allowStyleChoose', 'true');
    setShowConfirmModal(false);
    navigate('/stylechoose');
  }

  return (
    <div className="myPageFrame">
      <div>

        <div className="bannerBox" />
        <img src="/imgs/bannerImg.png" alt='배너이미지' className="bannerImg" />

        <div className="title">SERENDY</div>
        <div className="slogan">패션 추천 사이트, 슬로건 작성</div>


        <div className="myPageMenuBox">

          <div className='myPageMenuTxt'>마이페이지</div>

          <Link to="/mypagepersonalinfo" className="mypageLink">
            <span>개인 정보 수정</span>
            <img src="/imgs/Vector.png" className='mpmVectorImg' />
          </Link>

          <Link to="/mypagefavprod" className="mypageLink">
            <span>선호 상품 목록</span>
            <img src="/imgs/Vector.png" className='mpmVectorImg' />
          </Link>


          {/* 도움말 + 리셋 버튼 컨테이너 */}
          <button
            className="resetBtn"
            onClick={resetStyle}
          >
            추천 정보 리셋
          </button>
          <button className="helpBtn" onClick={() => setShowHelp(true)}>
            <img src="/imgs/help(white).png" className='helpImg' />
          </button>

        </div>

        {/* 도움말 모달 */}
        {showHelp && (
          <div className="modalOverlay" onClick={() => setShowHelp(false)}>
            <div className="modalBox" onClick={e => e.stopPropagation()}>
              <h3>추천 정보 리셋이란?</h3>
              <p>
                리셋을 누르면 기존에 선택했던 스타일 정보가 초기화되고, 새로운 스타일을 선택할 수 있습니다.
              </p>
              <button className="modalCloseBtn" onClick={() => setShowHelp(false)}>닫기</button>
            </div>
          </div>
        )}

        {/* 추천 정보 리셋 모달 */}
        {showConfirmModal && (
          <div className="modalOverlay" onClick={() => setShowConfirmModal(false)}>
            <div className="modalBox" onClick={(e) => e.stopPropagation()}>
              <h3>추천 정보 리셋 확인</h3>
              <p>
                선택하신 스타일은 "{userStyle.join(', ') || '없음'}"입니다.<br />
                추천 정보를 리셋하시겠습니까?
              </p>
              <div className="modalButtonGroup">
                <button className="modalConfirmBtn" onClick={handleConfirmReset}>확인</button>
                <button className="modalCloseBtn" onClick={() => setShowConfirmModal(false)}>취소</button>
              </div>
            </div>
          </div>
        )}

      </div>

    </div>
  )
}

export default MyPageMenu