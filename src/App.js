import { Routes, Route, useLocation } from 'react-router-dom';
import Join from './pages/Join';
import Login from './pages/Login';
import MyPagePersonalInfo from './pages/MyPagePersonalInfo';
import MyPageMenu from './pages/MyPageMenu';
import StyleChoose from './pages/StyleChoose';
import MainRecomdProd from './pages/MainRecomdProd';
import AllRecomdProd from './pages/AllRecomdProd';
import MyPageFavProd from './pages/MyPageFavProd';
import AllProd from './pages/AllProd';
import Header from './pages/Header';
import ProdDetail from './pages/ProdDetail';
import Footer from './pages/Footer';

function App() {
  const location = useLocation();
  const hideHeaderPaths = ['/login', '/join', '/mypagemenu', '/mypagepersonalinfo', '/stylechoose'];

  const shouldHideHeader = hideHeaderPaths.includes(location.pathname);

  return (
    <div>
      {!shouldHideHeader && <Header />}

      <Routes>
        <Route path='/login' element={<Login />} />
        <Route path='/join' element={<Join />} />
        <Route path='/mypagemenu' element={<MyPageMenu />} />
        <Route path='/mypagepersonalinfo' element={<MyPagePersonalInfo />} />
        <Route path='/stylechoose' element={<StyleChoose />} />

        {/* 헤더 포함 페이지 */}
        <Route path='/mainrecomdprod' element={<MainRecomdProd />} />
        <Route path='/allrecomdprod' element={<AllRecomdProd />} />
        <Route path='/mypagefavprod' element={<MyPageFavProd />} />
        <Route path='/allprod' element={<AllProd />} />
        <Route path='/proddetail' element={<ProdDetail />} />
      </Routes>

      <Footer />
    </div>
  );
}

export default App;