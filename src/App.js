import { Routes, Route, useLocation, Navigate } from 'react-router-dom';
import Header from './components/pages/Header';
import Login from './pages/Login';
import Join from './pages/Join';
import MyPageMenu from './pages/MyPageMenu';
import MyPagePersonalInfo from './pages/MyPagePersonalInfo';
import StyleChoose from './pages/StyleChoose';
import MainRecomdProd from './pages/MainRecomdProd';
import AllRecomdProd from './pages/AllRecomdProd';
import MyPageFavProd from './pages/MyPageFavProd';
import AllProd from './pages/AllProd';
import ProdDetail from './pages/ProdDetail';
import SearchResultsProd from './pages/SearchResultsProd';
import Footer from './components/pages/Footer';
import ScrollToTop from './components/pages/ScrollToTop';
import PrivateRoute from './components/pages/PrivateRoute';
import PublicRoute from './components/pages/PublicRoute';
import StyleChooseRoute from './components/pages/StyleChooseRoute';

function App() {
  const location = useLocation();

  // 헤더 포함, 미포함 페이지 설정
  const hideHeaderPaths = ['/login', '/join', '/mypagemenu', '/mypagepersonalinfo', '/stylechoose'];
  const shouldHideHeader = hideHeaderPaths.includes(location.pathname);

  return (
    <div>
      {!shouldHideHeader && <Header />}

      <ScrollToTop />

      <Routes>

        {/* 메인페이지 설정 */}
        <Route path="/" element={
          localStorage.getItem('userId') ? (
            <Navigate to="/mainrecomdprod" replace />
          ) : (
            <Navigate to="/login" replace />
          )
        }
        />

        <Route element={<PublicRoute />}>
          <Route path='/login' element={<Login />} />
          <Route path='/join' element={<Join />} />
        </Route>

        <Route element={<StyleChooseRoute />}>
          <Route path='/stylechoose' element={<StyleChoose />} />
        </Route>

        <Route element={<PrivateRoute />}>
          <Route path='/mypagemenu' element={<MyPageMenu />} />
          <Route path='/mypagepersonalinfo' element={<MyPagePersonalInfo />} />
          <Route path='/mainrecomdprod' element={<MainRecomdProd />} />
          <Route path='/allrecomdprod' element={<AllRecomdProd />} />
          <Route path='/mypagefavprod' element={<MyPageFavProd />} />
          <Route path='/allprod' element={<AllProd />} />
          <Route path='/searchresultsprod' element={<SearchResultsProd />} />
          <Route path='/proddetail/:id' element={<ProdDetail />} />
        </Route>

      </Routes>

      <Footer />
    </div>
  );
}

export default App;