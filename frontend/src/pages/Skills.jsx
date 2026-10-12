import { useEffect, useState } from "react";
import { api } from "../api/client.js";

const blank = { name: "", proficiency: "BEGINNER" };

export default function Skills() {
  const [skills, setSkills] = useState([]);
  const [form, setForm] = useState(blank);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");

  async function load() {
    const data = await api("/api/skills");
    setSkills(data);
  }

  useEffect(() => {
    load().catch((err) => setError(err.message));
  }, []);

  async function onSubmit(event) {
    event.preventDefault();
    setError("");
    try {
      if (editingId) {
        await api(`/api/skills/${editingId}`, { method: "PUT", body: JSON.stringify(form) });
      } else {
        await api("/api/skills", { method: "POST", body: JSON.stringify(form) });
      }
      setForm(blank);
      setEditingId(null);
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  async function remove(id) {
    setError("");
    try {
      await api(`/api/skills/${id}`, { method: "DELETE" });
      await load();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="split">
      <form className="panel form" onSubmit={onSubmit}>
        <h1>{editingId ? "Edit skill" : "Add skill"}</h1>
        {error && <p className="error">{error}</p>}
        <label>
          Skill name
          <input required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
        </label>
        <label>
          Proficiency
          <select
            value={form.proficiency}
            onChange={(e) => setForm({ ...form, proficiency: e.target.value })}
          >
            <option value="BEGINNER">BEGINNER</option>
            <option value="INTERMEDIATE">INTERMEDIATE</option>
            <option value="ADVANCED">ADVANCED</option>
          </select>
        </label>
        <div className="actions">
          <button className="btn" type="submit">
            {editingId ? "Update" : "Add"}
          </button>
          {editingId && (
            <button
              className="btn btn-ghost"
              type="button"
              onClick={() => {
                setEditingId(null);
                setForm(blank);
              }}
            >
              Cancel
            </button>
          )}
        </div>
      </form>
      <section className="panel">
        <h2>Your skills</h2>
        {skills.length === 0 ? (
          <p className="muted">No skills yet.</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Level</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {skills.map((skill) => (
                <tr key={skill.id}>
                  <td>{skill.name}</td>
                  <td>{skill.proficiency}</td>
                  <td className="row-actions">
                    <button
                      className="btn btn-small"
                      type="button"
                      onClick={() => {
                        setEditingId(skill.id);
                        setForm({ name: skill.name, proficiency: skill.proficiency });
                      }}
                    >
                      Edit
                    </button>
                    <button className="btn btn-small btn-danger" type="button" onClick={() => remove(skill.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}
