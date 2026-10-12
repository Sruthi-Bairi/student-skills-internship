import { Route, Routes } from "react-router-dom";
import Layout from "./components/Layout.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import AdminApplications from "./pages/AdminApplications.jsx";
import AdminDashboard from "./pages/AdminDashboard.jsx";
import AdminInternships from "./pages/AdminInternships.jsx";
import AdminStudents from "./pages/AdminStudents.jsx";
import Applications from "./pages/Applications.jsx";
import Home from "./pages/Home.jsx";
import Internships from "./pages/Internships.jsx";
import Login from "./pages/Login.jsx";
import Profile from "./pages/Profile.jsx";
import Register from "./pages/Register.jsx";
import Skills from "./pages/Skills.jsx";
import StudentDashboard from "./pages/StudentDashboard.jsx";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute role="STUDENT">
              <StudentDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute role="STUDENT">
              <Profile />
            </ProtectedRoute>
          }
        />
        <Route
          path="/skills"
          element={
            <ProtectedRoute role="STUDENT">
              <Skills />
            </ProtectedRoute>
          }
        />
        <Route
          path="/internships"
          element={
            <ProtectedRoute role="STUDENT">
              <Internships />
            </ProtectedRoute>
          }
        />
        <Route
          path="/applications"
          element={
            <ProtectedRoute role="STUDENT">
              <Applications />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/students"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminStudents />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/internships"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminInternships />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/applications"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminApplications />
            </ProtectedRoute>
          }
        />
      </Route>
    </Routes>
  );
}
