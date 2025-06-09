import { createBrowserRouter } from 'react-router-dom';
import AppLayout from '../layouts/AppLayout';
import Login from '../pages/Login/Login';
import OrgList from '../pages/Org/OrgList';
import UserList from '../pages/User/UserList';
import KnowledgeList from '../pages/Knowledge/KnowledgeList';
import DocumentList from '../pages/Knowledge/DocumentList';
import Error from '../pages/Error';

export const router = createBrowserRouter([
  { path: '/', element: <Login /> },
  {
    element: <AppLayout />,
    children: [
      { path: '/org', element: <OrgList /> },
      { path: '/user', element: <UserList /> },
      { path: '/knowledge', element: <KnowledgeList /> },
      { path: '/documents', element: <DocumentList /> },
    ],
  },
  { path: '*', element: <Error /> },
]);
