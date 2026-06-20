import { createBrowserRouter } from 'react-router-dom';
import LoginPage from './pages/auth/LoginPage';
import SignupPage from './pages/auth/SignupPage';
import SessionPage from './pages/pos/SessionPage';
import TableViewPage from './pages/pos/TableViewPage';
import OrderViewPage from './pages/pos/OrderViewPage';
import OrdersListPage from './pages/pos/OrdersListPage';
import BookingPage from './pages/admin/BookingPage';
import KdsPage from './pages/kds/KdsPage';
import CustomerDisplayPage from './pages/customerDisplay/CustomerDisplayPage';
import SplashPage from './pages/selfOrder/SplashPage';
import ProductsPage from './pages/admin/ProductsPage';
import CategoriesPage from './pages/admin/CategoriesPage';
import PaymentMethodsPage from './pages/admin/PaymentMethodsPage';
import CouponsPromotionsPage from './pages/admin/CouponsPromotionsPage';
import UsersPage from './pages/admin/UsersPage';
import ReportsPage from './pages/admin/ReportsPage';
import SelfOrderConfigPage from './pages/admin/SelfOrderConfigPage';
import ProtectedRoute from './components/common/ProtectedRoute';

export const router = createBrowserRouter([
  { path: '/', element: <LoginPage /> },
  { path: '/login', element: <LoginPage /> },
  { path: '/signup', element: <SignupPage /> },
  { path: '/pos', element: <ProtectedRoute><SessionPage /></ProtectedRoute> },
  { path: '/pos/floor', element: <ProtectedRoute><TableViewPage /></ProtectedRoute> },
  { path: '/pos/order/:tableId', element: <ProtectedRoute><OrderViewPage /></ProtectedRoute> },
  { path: '/pos/orders', element: <ProtectedRoute><OrdersListPage /></ProtectedRoute> },
  { path: '/pos/customers', element: <ProtectedRoute><BookingPage /></ProtectedRoute> },
  { path: '/kds', element: <ProtectedRoute><KdsPage /></ProtectedRoute> },
  { path: '/customer-display', element: <ProtectedRoute><CustomerDisplayPage /></ProtectedRoute> },
  { path: '/s/:token', element: <SplashPage /> },
  { path: '/admin/products', element: <ProtectedRoute><ProductsPage /></ProtectedRoute> },
  { path: '/admin/categories', element: <ProtectedRoute><CategoriesPage /></ProtectedRoute> },
  { path: '/admin/payment-methods', element: <ProtectedRoute><PaymentMethodsPage /></ProtectedRoute> },
  { path: '/admin/coupons', element: <ProtectedRoute><CouponsPromotionsPage /></ProtectedRoute> },
  { path: '/admin/users', element: <ProtectedRoute><UsersPage /></ProtectedRoute> },
  { path: '/admin/reports', element: <ProtectedRoute><ReportsPage /></ProtectedRoute> },
  { path: '/admin/self-order-config', element: <ProtectedRoute><SelfOrderConfigPage /></ProtectedRoute> },
]);
