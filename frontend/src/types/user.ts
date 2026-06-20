export interface User {
  id: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'EMPLOYEE';
  status: 'ACTIVE' | 'ARCHIVED';
  createdAt: string;
}
