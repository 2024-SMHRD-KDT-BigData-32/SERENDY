import { Navigate, Outlet } from 'react-router-dom';

const PublicRoute = () => {
  const userId = localStorage.getItem('userId');
  return userId ? <Navigate to="/mainrecomdprod" replace /> : <Outlet />;
};

export default PublicRoute;