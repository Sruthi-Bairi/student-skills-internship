import { useEffect, useState } from "react";
import { api } from "../api/client.js";

export default function AdminStudents() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api("/api/admin/students")
      .then(setItems)
      .catch((err) => setError(err.message));
  }, []);

  return (
    <section className="panel">
      <h1>Registered students</h1>
      {error && <p className="error">{error}</p>}
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>College</th>
            <th>Course</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id}>
              <td>{item.fullName || "-"}</td>
              <td>{item.email}</td>
              <td>{item.college || "-"}</td>
              <td>{item.course || "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
