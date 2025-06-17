import { Navigate, Outlet } from 'react-router-dom';

const StyleChooseRoute = () => {
  const tempId = localStorage.getItem('tempId');
  const userId = localStorage.getItem('userId');
  const allowStyleChoose = localStorage.getItem('allowStyleChoose');

  if ((tempId || userId) && allowStyleChoose === 'true') {
    return <Outlet />;
  } else {
    alert('잘못된 접근입니다. 회원가입 또는 추천 정보 리셋 후 다시 접근해주세요.');
    return <Navigate to="/login" replace />;
  }
};

export default StyleChooseRoute;