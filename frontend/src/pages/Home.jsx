import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client.js";
import { useAuth } from "../auth/AuthContext.jsx";

export default function Home() {
  const { user } = useAuth();
  const [health, setHealth] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api("/api/health")
      .then(setHealth)
      .catch((err) => setError(err.message));
  }, []);

  return (
    <div className="stack">
      <nav
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          flexWrap: "wrap",
          gap: "16px",
          padding: "20px 0",
        }}
      >
        <h2 style={{ margin: 0, color: "#2563eb" }}>
          SSIMS
        </h2>

        <div className="actions">
          <Link to="/" style={{ textDecoration: "none" }}>
            Home
          </Link>
          <Link to="/login" style={{ textDecoration: "none" }}>
            Login
          </Link>
          <Link to="/register" style={{ textDecoration: "none" }}>
            Register
          </Link>
        </div>
      </nav>

      <section
        className="hero"
        style={{
          padding: "48px 24px",
          borderRadius: "18px",
          background:
            "linear-gradient(135deg, #eff6ff, #ffffff)",
          alignItems: "center",
        }}
      >
        <div>
          <p className="eyebrow">
            YOUR FUTURE STARTS HERE
          </p>

          <h1
            style={{
              fontSize: "clamp(32px, 5vw, 52px)",
              lineHeight: 1.15,
              color: "#172554",
            }}
          >
            Build Your Skills,
            <br />
            Launch Your Career
          </h1>

          <p className="lede">
            Student Skills and Internship Management System
            (SSIMS) helps students manage their profiles,
            showcase technical skills, explore internships,
            and track their applications in one place.
          </p>

          <div className="actions">
            {user ? (
              <Link
                className="btn"
                to={user.role === "ADMIN" ? "/admin" : "/dashboard"}
              >
                Go to Dashboard
              </Link>
            ) : (
              <>
                <Link className="btn" to="/register">
                  Get Started
                </Link>

                <Link className="btn btn-ghost" to="/login">
                  Login to SSIMS
                </Link>
              </>
            )}
          </div>
        </div>

        <aside
          className="panel"
          style={{
            background: "#ffffff",
            padding: "28px",
            borderRadius: "16px",
            boxShadow: "0 10px 30px rgba(37, 99, 235, 0.10)",
          }}
        >
          <div style={{ fontSize: "42px" }}>🎓</div>
          <h2 style={{ color: "#1d4ed8" }}>
            Your Career Toolkit
          </h2>
          <p className="muted">
            Everything you need to take the next step
            toward your professional goals.
          </p>

          <ul className="checklist">
            <li>Build your student profile</li>
            <li>Showcase your technical skills</li>
            <li>Explore internship opportunities</li>
            <li>Track your application status</li>
          </ul>
        </aside>
      </section>

      <section className="stack">
        <div style={{ textAlign: "center" }}>
          <p className="eyebrow">WHY CHOOSE SSIMS?</p>
          <h2>Everything in One Place</h2>
          <p className="muted">
            A simple platform to organize your internship journey.
          </p>
        </div>

        <div className="stats">
          <article className="stat">
            <div style={{ fontSize: "28px" }}>👤</div>
            <strong>Student Profile</strong>
            <span>Maintain your academic details.</span>
          </article>

          <article className="stat">
            <div style={{ fontSize: "28px" }}>💡</div>
            <strong>Technical Skills</strong>
            <span>Showcase your knowledge and abilities.</span>
          </article>

          <article className="stat">
            <div style={{ fontSize: "28px" }}>💼</div>
            <strong>Internships</strong>
            <span>Explore opportunities and apply.</span>
          </article>

          <article className="stat">
            <div style={{ fontSize: "28px" }}>📋</div>
            <strong>Application Tracking</strong>
            <span>Follow your application progress.</span>
          </article>
        </div>
      </section>

      <section className="panel">
        <h2>System Status</h2>
        {health ? (
          <p>
            <strong>{health.status}</strong> — {health.application}
          </p>
        ) : (
          <p className={error ? "error" : "muted"}>
            {error || "Checking backend connection..."}
          </p>
        )}
      </section>

      <footer
        style={{
          textAlign: "center",
          padding: "20px 0",
          color: "#64748b",
        }}
      >
        Student Skills and Internship Management System · SSIMS
      </footer>
    </div>
  );
}