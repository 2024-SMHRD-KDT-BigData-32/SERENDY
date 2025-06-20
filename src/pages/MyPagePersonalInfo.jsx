import axios from 'axios';
import '../css/MyPagePersonalInfo.css'
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

const MyPage = () => {
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  const [userInfo, setUserInfo] = useState({
    ageGroup: '',
    gender: '',
    id: '',
    name: '',
    pw: ''
  });

  const [oldPw, setOldPw] = useState('');
  const [newPw, setNewPw] = useState('');
  const [confirmPw, setConfirmPw] = useState('');

  // 회원 정보 불러오기
  useEffect(() => {
    const getUserInfo = async () => {
      try {
        const res = await axios.get(`/api/users/profile/${userId}`);
        setUserInfo(res.data);
        console.log(res.data)
      } catch (error) {
        console.error('회원 정보 불러오기 실패:', error);
        alert('회원 정보를 불러오는데 실패했습니다.');
      }
    };

    if (userId) {
      getUserInfo();
    }
  }, [userId]);

  // 회원 정보 수정
  const editUserInfo = async () => {

    if (!oldPw) {
      alert("본인 확인을 위해 기존 비밀번호를 입력해주세요.");
      return;
    }

    if (oldPw !== userInfo.pw) {
      alert("기존 비밀번호가 일치하지 않습니다.");
      return;
    }

    if (newPw || confirmPw) {
      if (newPw !== confirmPw) {
        alert("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        return;
      }
    }

    try {
      await axios.patch(`/api/users/edit/${userId}`, {
        name: userInfo.name,
        ageGroup: userInfo.ageGroup,
        gender: userInfo.gender,
        pw: newPw ? newPw : userInfo.pw
      });

      alert('회원 정보가 수정되었습니다.');
      setOldPw('');
      setNewPw('');
      setConfirmPw('');
    } catch (error) {
      console.error('회원 정보 수정 실패:', error);
      alert('회원 정보 수정 중 오류가 발생했습니다.');
    }
  };

  // 회원 탈퇴
  const deleteUser = async () => {
    const confirmDelete = window.confirm("정말로 회원 탈퇴하시겠습니까?");
    if (!confirmDelete) return;

    try {
      await axios.delete(`/api/users/user/${userId}`);
      alert("회원 탈퇴가 완료되었습니다.");
      localStorage.removeItem('userId');
      navigate('/login');
    } catch (error) {
      console.error("회원 탈퇴 실패 :", error);
      alert("회원 탈퇴 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="myPageFrame">
      <div>

        <div className="bannerBox" />
        <img src="/imgs/bannerImg.png" alt='배너이미지' className='bannerImg' />

        <div className="title">SERENDY</div>
        <div className="slogan">패션 추천 사이트, 슬로건 작성</div>

        <div className="myPageFormBox">

          <div className='myPageFormTxt'>마이페이지</div>

          <input
            type="text"
            placeholder="이름"
            value={userInfo.name}
            onChange={(e) => setUserInfo({ ...userInfo, name: e.target.value })}
          />

          <input
            type="text"
            placeholder="아이디"
            value={userInfo.id}
            readOnly
          />

          <input
            type="password"
            placeholder="기존 비밀번호"
            value={oldPw}
            onChange={(e) => setOldPw(e.target.value)}
          />

          <input
            type="password"
            placeholder="새 비밀번호"
            value={newPw}
            onChange={(e) => setNewPw(e.target.value)}
          />

          <input
            type="password"
            placeholder="비밀번호 확인"
            value={confirmPw}
            onChange={(e) => setConfirmPw(e.target.value)}
          />

          <select
            value={userInfo.ageGroup || ''}
            onChange={(e) => setUserInfo({ ...userInfo, ageGroup: e.target.value })}
          >
            <option value="" disabled>연령대 선택</option>
            <option value="20대">20대</option>
            <option value="30대">30대</option>
            <option value="40대">40대</option>
            <option value="50대">50대</option>
          </select>

          <div className='myPageGender'>
            <label>성별</label>

            <label>
              <input
                type="radio"
                name='gender'
                value='F'
                checked={userInfo.gender === 'F'}
                onChange={(e) => setUserInfo({ ...userInfo, gender: e.target.value })}
              />
              여성
            </label>

            <label>
              <input
                type="radio"
                name='gender'
                value='M'
                checked={userInfo.gender === 'M'}
                onChange={(e) => setUserInfo({ ...userInfo, gender: e.target.value })}
              />
              남성
            </label>

          </div>

          <button className='editUserInfoBtn' onClick={editUserInfo}>수정</button>
          <button onClick={deleteUser} className='delUserBtn'>회원탈퇴</button>

        </div>

      </div>
    </div>
  )
}

export default MyPage