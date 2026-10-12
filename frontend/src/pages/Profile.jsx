import { useEffect, useState } from "react";
import { api } from "../api/client.js";

const empty = {
  fullName: "",
  college: "",
  course: "",
  yearOfStudy: "",
  phone: "",
  bio: "",
};

export default function Profile() {
  const [form, setForm] = useState(empty);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    api("/api/profile")
      .then((data) =>
        setForm({
          fullName: data.fullName || "",
          college: data.college || "",
          course: data.course || "",
          yearOfStudy: data.yearOfStudy || "",
          phone: data.phone || "",
          bio: data.bio || "",
        })
      )
      .catch((err) => setError(err.message));
  }, []);

  async function onSubmit(event) {
    event.preventDefault();
    setError("");
    setMessage("");
    try {
      await api("/api/profile", {
        method: "PUT",
        body: JSON.stringify({
          ...form,
          yearOfStudy: form.yearOfStudy === "" ? null : Number(form.yearOfStudy),
        }),
      });
      setMessage("Profile saved.");
    } catch (err) {
      setError(err.message);
    }
  }

  function update(field) {
    return (event) => setForm({ ...form, [field]: event.target.value });
  }

  return (
    <form className="panel form" onSubmit={onSubmit}>
      <h1>Student profile</h1>
      {error && <p className="error">{error}</p>}
      {message && <p className="success">{message}</p>}
      <label>
        Full name
        <input required value={form.fullName} onChange={update("fullName")} />
      </label>
      <label>
        College
        <input value={form.college} onChange={update("college")} />
      </label>
      <label>
        Course
        <input value={form.course} onChange={update("course")} />
      </label>
      <label>
        Year of study
        <input type="number" min="1" max="6" value={form.yearOfStudy} onChange={update("yearOfStudy")} />
      </label>
      <label>
        Phone
        <input value={form.phone} onChange={update("phone")} />
      </label>
      <label>
        Bio
        <textarea rows="4" value={form.bio} onChange={update("bio")} />
      </label>
      <button className="btn" type="submit">
        Save profile
      </button>
    </form>
  );
}
