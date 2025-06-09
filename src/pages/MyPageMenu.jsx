import { Link } from 'react-router-dom'
import '../css/MyPageMenu.css'
import { useState } from 'react';

const MyPageMenu = () => {
    const [showHelp, setShowHelp] = useState(false);

    const handleReset = () => {
        // 추천 정보 리셋 기능 구현
        alert('추천 정보가 리셋되었습니다!');
    };
  return (
      <div className="myPageFrame">
        <div>

            <div className="bannerBox"/>
            <img src="/imgs/배너이미지1.png" className="bannerImg"/>

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
                <button className="resetBtn" onClick={handleReset}>
                    추천 정보 리셋
                </button>
                <button className="helpBtn" onClick={() => setShowHelp(true)}>
                    <img src="/imgs/help(white).png" className='helpImg'/>
                </button>

            </div>

            {/* 도움말 모달 */}
            {showHelp && (
                <div className="modalOverlay" onClick={() => setShowHelp(false)}>
                    <div className="modalBox" onClick={e => e.stopPropagation()}>
                        <h3>추천 정보 리셋이란?</h3>
                        <p>
                        리셋을 누르면 기존의 추천 정보가 초기화되고, 새로운 추천을 받기 위한 상태로 되돌아갑니다.
                        </p>
                        <button className="modalCloseBtn" onClick={() => setShowHelp(false)}>닫기</button>
                    </div>
                </div>
            )}
        
        </div>

    </div>
  )
}

export default MyPageMenu