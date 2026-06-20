import client from './client';
import { User } from '../types/user';

export interface AuthResponseData {
  token: string;
  user: User;
}

export const authApi = {
  login: async (credentials: any): Promise<AuthResponseData> => {
    const response = await client.post<AuthResponseData>('/auth/login', credentials);
    return response.data;
  },
  signup: async (userData: any): Promise<AuthResponseData> => {
    const response = await client.post<AuthResponseData>('/auth/signup', userData);
    return response.data;
  },
};
