// Script que exibe itens disponíveis e permite solicitar reservas

// Obtém o ID do funcionário salvo no navegador (login)
const funcionarioId = localStorage.getItem("id_funcionario") || 1;
let statusFuncionario = ""; // NOVO: Armazena o status do funcionário

// Função que carrega todos os itens do banco e exibe os cards
function carregarItens() {
  fetch("php/get_itens.php")
    .then(res => res.json())
    .then(itens => {
      const grid = document.getElementById("grid-itens");
      const filtroNome = document.getElementById("filtro-nome").value.toLowerCase();
      const filtroStatus = document.getElementById("filtro-status").value;

      grid.innerHTML = "";

      if (!Array.isArray(itens)) {
        console.error("Resposta inválida:", itens);
        return;
      }

      itens.forEach(item => {
        const nome = (item.nome || "").toLowerCase();
        const categoria = item.categoria || "Sem categoria";
        const statusDisponivel = item.disponivel == 1 || item.disponivel === true;

        const nomeCondiz = nome.includes(filtroNome);
        const statusCondiz =
          filtroStatus === "todos" ||
          (filtroStatus === "disponivel" && statusDisponivel) ||
          (filtroStatus === "indisponivel" && !statusDisponivel);

        if (nomeCondiz && statusCondiz) {
          const card = document.createElement("div");
          card.className = "card";

          card.innerHTML = `
            <h3>${item.nome}</h3>
            <p>${item.categoria}</p>
          `;

          const botao = document.createElement("button");

          if (statusDisponivel) {
            botao.textContent = "Reservar";
            botao.className = "btn-disponivel";
            botao.onclick = () => reservarItem(item.id);
          } else {
            botao.textContent = "Indisponível";
            botao.className = "btn-indisponivel";
            botao.disabled = true;
          }

          card.appendChild(botao);
          grid.appendChild(card);
        }
      });
    })
    .catch(erro => console.error("Erro ao buscar itens:", erro));
}

// Solicita a reserva de um item
function reservarItem(idItem) {
  const status = localStorage.getItem("status_func");

  if (status && status.toLowerCase() === "negativo") {
    alert("Seu status está negativo. Por favor, entre em contato com o administrador.");
    return;
  }

  fetch("php/reservar_item.php", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: `id_item=${idItem}&id_funcionario=${localStorage.getItem("id_funcionario")}`
  })
  .then(res => res.json()) // ← converte para objeto
  .then(ret => {
    if (ret.status === "ok") {
      alert("Reserva solicitada com sucesso!");
    } else {
      alert(ret.mensagem || "Erro ao solicitar reserva.");
    }
  })
  .catch(err => {
    console.error("Erro ao reservar:", err);
    alert("Erro de conexão com o servidor.");
  });
}

// Detecta o gênero do nome do funcionário para exibir imagem de perfil correspondente
function detectarGenero(nome) {
  const sufixosFemininos = ["a", "ana", "ela", "ia", "ina", "ine", "isa", "eva", "íris"];
  const nomeMinusculo = nome.toLowerCase();
  return sufixosFemininos.some(suf => nomeMinusculo.endsWith(suf)) ? "feminino" : "masculino";
}

// Carrega imagem e informações do funcionário
fetch('php/get_funcionario.php?id=' + funcionarioId)
  .then(res => res.json())
  .then(func => {
    statusFuncionario = func.status_func; // define status para bloqueio de reserva

    // Atualiza elementos do perfil apenas se existirem na página
    const nomeEl = document.getElementById('nome-usuario');
    const reEl = document.getElementById('re-usuario');
    const img = document.querySelector(".foto img");

    if (nomeEl) nomeEl.textContent = func.nome;
    if (reEl) reEl.textContent = 'RE: ' + func.re;
    if (img) {
      const genero = detectarGenero(func.nome);
      img.src = genero === "feminino" ? "img/perfil_feminino.png" : "img/perfil_masculino.png";
    }
  });

// Inicia carregamento da página
window.onload = () => {
  carregarItens();
};

// Filtros de busca
document.getElementById("filtro-nome").addEventListener("input", carregarItens);
document.getElementById("filtro-status").addEventListener("change", carregarItens);