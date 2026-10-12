import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");

  async function onSubmit(event) {
    event.preventDefault();
    setError("");
    try {
      const user = await login(form.email, form.password);
      navigate(user.role === "ADMIN" ? "/admin" : "/dashboard");
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <form className="panel form" onSubmit={onSubmit}>
      <h1>Sign in</h1>
      {error && <p className="error">{error}</p>}
      <label>
        Email
        <input
          type="email"
          required
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
        />
      </label>
      <label>
        Password
        <input
          type="password"
          required
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
        />
      </label>
      <button className="btn" type="submit">
        Login
      </button>
      <p className="muted">
        New student? <Link to="/register">Create an account</Link>
      </p>
      <p className="muted">Admin demo: admin@ssims.local / Admin@123</p>
    </form>
  );
}
