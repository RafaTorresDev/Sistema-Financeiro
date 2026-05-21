import { useState, useEffect } from 'react'

const API = 'http://localhost:8080/transacoes'

function formatCurrency(value) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value)
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const [year, month, day] = dateStr.split('-')
  return `${day}/${month}/${year}`
}

export default function App() {
  const [transacoes, setTransacoes] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState(null)
  const [deletando, setDeletando] = useState(null)

  const [form, setForm] = useState({ valor: '', categoria: '', data: '' })
  const [tipo, setTipo] = useState('entrada')
  const [salvando, setSalvando] = useState(false)
  const [formErro, setFormErro] = useState(null)

  async function carregar() {
    try {
      setLoading(true)
      setErro(null)
      const res = await fetch(API)
      if (!res.ok) throw new Error('Erro ao carregar transações')
      const data = await res.json()
      setTransacoes(data)
    } catch (e) {
      setErro(e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { carregar() }, [])

  async function handleSubmit(e) {
    e.preventDefault()
    if (!form.valor || !form.categoria || !form.data) {
      setFormErro('Preencha todos os campos.')
      return
    }
    try {
      setSalvando(true)
      setFormErro(null)
      const res = await fetch(API, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          valor: tipo === 'saida' ? -Math.abs(parseFloat(form.valor)) : Math.abs(parseFloat(form.valor)),
          categoria: form.categoria,
          data: form.data,
        }),
      })
      if (!res.ok) throw new Error('Erro ao salvar transação')
      setForm({ valor: '', categoria: '', data: '' })
      setTipo('entrada')
      await carregar()
    } catch (e) {
      setFormErro(e.message)
    } finally {
      setSalvando(false)
    }
  }

  async function handleDeletar(id) {
    try {
      setDeletando(id)
      const res = await fetch(`${API}/${id}`, { method: 'DELETE' })
      if (!res.ok) throw new Error('Erro ao deletar')
      await carregar()
    } catch (e) {
      setErro(e.message)
    } finally {
      setDeletando(null)
    }
  }

  const saldo = transacoes.reduce((acc, t) => acc + t.valor, 0)
  const positivas = transacoes.filter(t => t.valor >= 0).length
  const negativas = transacoes.filter(t => t.valor < 0).length

  return (
    <div className="app">
      <div className="grid-bg" />

      <header className="header">
        <div className="header-inner">
          <div className="logo">
            <span className="logo-icon">◈</span>
            <span className="logo-text">FINTRACK</span>
          </div>
          <span className="header-sub">Sistema Financeiro</span>
        </div>
      </header>

      <main className="main">
        <section className="saldo-section" style={{ '--delay': '0s' }}>
          <div className="saldo-label">SALDO TOTAL</div>
          <div className={`saldo-valor ${saldo >= 0 ? 'positivo' : 'negativo'}`}>
            {formatCurrency(saldo)}
          </div>
          <div className="saldo-stats">
            <span className="stat entrada">↑ {positivas} entradas</span>
            <span className="divider">|</span>
            <span className="stat saida">↓ {negativas} saídas</span>
            <span className="divider">|</span>
            <span className="stat total">{transacoes.length} total</span>
          </div>
        </section>

        <section className="form-section" style={{ '--delay': '0.1s' }}>
          <h2 className="section-title">Nova Transação</h2>
          <form className="form" onSubmit={handleSubmit}>
            <div className="tipo-toggle">
              <button
                type="button"
                className={`tipo-btn entrada ${tipo === 'entrada' ? 'active' : ''}`}
                onClick={() => setTipo('entrada')}
              >
                ↑ Entrada
              </button>
              <button
                type="button"
                className={`tipo-btn saida ${tipo === 'saida' ? 'active' : ''}`}
                onClick={() => setTipo('saida')}
              >
                ↓ Saída
              </button>
            </div>
            <div className="form-row">
              <div className="field">
                <label className="field-label">VALOR</label>
                <input
                  className="field-input"
                  type="number"
                  step="0.01"
                  placeholder="0.00"
                  value={form.valor}
                  onChange={e => setForm(f => ({ ...f, valor: e.target.value }))}
                />
              </div>
              <div className="field">
                <label className="field-label">DATA</label>
                <input
                  className="field-input"
                  type="date"
                  value={form.data}
                  onChange={e => setForm(f => ({ ...f, data: e.target.value }))}
                />
              </div>
            </div>
            <div className="field">
              <label className="field-label">CATEGORIA</label>
              <input
                className="field-input"
                type="text"
                placeholder="ex: Salário, Alimentação, Transporte..."
                value={form.categoria}
                onChange={e => setForm(f => ({ ...f, categoria: e.target.value }))}
              />
            </div>
            {formErro && <p className="form-erro">{formErro}</p>}
            <button className="btn-submit" type="submit" disabled={salvando}>
              {salvando ? 'Salvando...' : '+ Adicionar Transação'}
            </button>
          </form>
        </section>

        <section className="lista-section" style={{ '--delay': '0.2s' }}>
          <h2 className="section-title">Extrato</h2>

          {erro && <div className="erro-box">{erro}</div>}

          {loading ? (
            <div className="loading">
              <span className="loading-dot" />
              <span className="loading-dot" />
              <span className="loading-dot" />
            </div>
          ) : transacoes.length === 0 ? (
            <div className="empty">Nenhuma transação registrada.</div>
          ) : (
            <ul className="lista">
              {transacoes.map((t, i) => (
                <li
                  key={t.id}
                  className="item"
                  style={{ '--item-delay': `${i * 0.05}s` }}
                >
                  <div className={`item-indicator ${t.valor >= 0 ? 'pos' : 'neg'}`} />
                  <div className="item-info">
                    <span className="item-categoria">{t.categoria}</span>
                    <span className="item-data">{formatDate(t.data)}</span>
                  </div>
                  <span className={`item-valor ${t.valor >= 0 ? 'pos' : 'neg'}`}>
                    {formatCurrency(t.valor)}
                  </span>
                  <button
                    className="btn-deletar"
                    onClick={() => handleDeletar(t.id)}
                    disabled={deletando === t.id}
                    title="Deletar"
                  >
                    {deletando === t.id ? '...' : '✕'}
                  </button>
                </li>
              ))}
            </ul>
          )}
        </section>
      </main>
    </div>
  )
}
