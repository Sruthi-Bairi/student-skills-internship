import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client.js";
import { useAuth } from "../auth/AuthContext.jsx";
import StatusBadge from "../components/StatusBadge.jsx";

export default function StudentDashboard() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [skills, setSkills] = useState([]);
  const [applications, setApplications] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api("/api/profile"),
      api("/api/skills"),
      api("/api/applications/me"),
    ])
      .then(([p, s, a]) => {
        setProfile(p);
        setSkills(s);
        setApplications(a);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const pending = applications.filter(
    (item) => item.status === "PENDING"
  ).length;

  return (
    <div className="stack">
      <section className="panel">
        <p>STUDENT SKILLS & INTERNSHIP MANAGEMENT SYSTEM</p>
        <h1>
          Welcome back{user?.fullName ? `, ${user.fullName}` : ""}! 🎓
        </h1>
        <p>
          Build your skills, discover opportunities, and take the
          next step towards your dream career.
        </p>
        <Link to="/internships">Explore Internships →</Link>
      </section>

      {error && <p className="error">{error}</p>}

      {loading ? (
        <p className="muted">Loading your dashboard...</p>
      ) : (
        <>
          <div className="stats">
            <article className="stat">
              <span>✦ Skills Added</span>
              <strong>{skills.length}</strong>
              <Link to="/skills">Manage Skills →</Link>
            </article>

            <article className="stat">
              <span>▤ Applications</span>
              <strong>{applications.length}</strong>
              <Link to="/applications">View Applications →</Link>
            </article>

            <article className="stat">
              <span>◷ Pending Applications</span>
              <strong>{pending}</strong>
              <Link to="/applications">Track Progress →</Link>
            </article>

            <article className="stat">
              <span>♙ Profile</span>
              <strong>{profile?.college ? "Active" : "Incomplete"}</strong>
              <Link to="/profile">Update Profile →</Link>
            </article>
          </div>

          <section className="panel">
            <h2>👤 Your Career Profile</h2>
            <p>
              <strong>College:</strong>{" "}
              {profile?.college || "Add your college details"}
            </p>
            <p>
              <strong>Course:</strong>{" "}
              {profile?.course || "Add your course details"}
            </p>
            <Link to="/profile">Complete Your Profile →</Link>
          </section>

          <section className="panel">
            <h2>🚀 Your Skills</h2>

            {skills.length === 0 ? (
              <p className="muted">
                No skills added yet. Start building your professional profile!
              </p>
            ) : (
              <div className="stats">
                {skills.slice(0, 8).map((skill, index) => (
                  <article className="stat" key={skill.id ?? index}>
                    <strong>
                      {typeof skill === "string"
                        ? skill
                        : skill.name ?? skill.skillName ?? "Skill"}
                    </strong>
                  </article>
                ))}
              </div>
            )}

            <Link to="/skills">Manage Your Skills →</Link>
          </section>

          <section className="panel">
            <h2>💼 Recent Applications</h2>

            {applications.length === 0 ? (
              <p className="muted">
                You haven't applied for any internships yet.{" "}
                <Link to="/internships">Browse internships →</Link>
              </p>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Internship</th>
                    <th>Company</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {applications.slice(0, 5).map((item) => (
                    <tr key={item.id}>
                      <td>{item.internshipTitle}</td>
                      <td>{item.company}</td>
                      <td>
                        <StatusBadge status={item.status} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}

            <p>
              <Link to="/applications">View All Applications →</Link>
            </p>
          </section>

          <section className="panel">
            <h2>⚡ Quick Actions</h2>
            <div className="stats">
              <article className="stat">
                <strong>Find Opportunities</strong>
                <p>Discover internships that match your goals.</p>
                <Link to="/internships">Browse Internships →</Link>
              </article>

              <article className="stat">
                <strong>Improve Your Profile</strong>
                <p>Keep your education and career details updated.</p>
                <Link to="/profile">Edit Profile →</Link>
              </article>

              <article className="stat">
                <strong>Grow Your Skills</strong>
                <p>Show employers what you can do.</p>
                <Link to="/skills">Update Skills →</Link>
              </article>
            </div>
          </section>

          <p className="muted">
            ✨ Learn | Build Skills | Get Internships | Build Your Future
          </p>
        </>
      )}
    </div>
  );
}