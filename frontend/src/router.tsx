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

export const router = createBrowserRouter([
  { path: '/', element: <LoginPage /> },
  { path: '/login', element: <LoginPage /> },
  { path: '/signup', element: <SignupPage /> },
  { path: '/pos', element: <SessionPage /> },
  { path: '/pos/floor', element: <TableViewPage /> },
  { path: '/pos/order/:tableId', element: <OrderViewPage /> },
  { path: '/pos/orders', element: <OrdersListPage /> },
  { path: '/pos/customers', element: <BookingPage /> },
  { path: '/kds', element: <KdsPage /> },
  { path: '/customer-display', element: <CustomerDisplayPage /> },
  { path: '/s/:token', element: <SplashPage /> },
  { path: '/admin/products', element: <ProductsPage /> },
  { path: '/admin/categories', element: <CategoriesPage /> },
  { path: '/admin/payment-methods', element: <PaymentMethodsPage /> },
  { path: '/admin/coupons', element: <CouponsPromotionsPage /> },
  { path: '/admin/users', element: <UsersPage /> },
  { path: '/admin/reports', element: <ReportsPage /> },
  { path: '/admin/self-order-config', element: <SelfOrderConfigPage /> },
]);
