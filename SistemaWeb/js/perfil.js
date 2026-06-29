  // Script que carrega as reservas do usuário e permite cancelamentos
  
  // Obtém o ID do funcionário armazenado no navegador
  const funcionarioId = localStorage.getItem('id_funcionario') || 1;

  // Carrega o histórico de reservas e preenche a tabela com opção de cancelar se for pendente
  function carregarHistoricoReservas() {
    fetch('php/get_reservas_por_usuario.php?id_funcionario=' + funcionarioId)
      .then(res => res.json())
      .then(dados => {
        if (!Array.isArray(dados)) {
          alert('Erro ao carregar reservas.');
          return;
        }

        const tbody = document.getElementById('historico-body');
        tbody.innerHTML = '';
        dados.forEach(r => {
          const tr = document.createElement('tr');
          tr.innerHTML = `
            <td>${r.Data_Reserva}</td>
            <td>${r.Nome_Item}</td>
            <td>${r.Categoria}</td>
            <td>${r.Status_Reserva.toLowerCase()}</td>
            <td>${r.Observacoes || '-'}</td>
          `;

          if (r.Status_Reserva.toLowerCase() === 'pendente') {
            const tdCancelar = document.createElement('td');
            const btnCancelar = document.createElement('button');
            btnCancelar.textContent = "Cancelar";
            btnCancelar.className = "btn-cancelar";
            btnCancelar.onclick = () => cancelarReserva(r.ID_Reserva); // <- se r.ID_Reserva for undefined, nada acontece
            tdCancelar.appendChild(btnCancelar);
            tr.appendChild(tdCancelar);
          }

          tbody.appendChild(tr);
        });
      });
  }

  // Busca o nome e o RE do funcionário para exibir no topo do perfil
  fetch('php/get_funcionario.php?id=' + funcionarioId)
    .then(res => res.json())
    .then(func => {
      document.getElementById('nome-usuario').textContent = func.nome;
      document.getElementById('re-usuario').textContent = 'RE: ' + func.re;
      document.getElementById('status-usuario').textContent = 'Status: ' + func.status_func;
    });

  // Função para cancelar reserva pendente
  function cancelarReserva(idReserva) {
    if (!confirm("Deseja realmente cancelar esta reserva?")) return;

    fetch("php/cancelar_reserva.php", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: `id=${idReserva}`
    })
      .then(res => res.text())
      .then(ret => {
        if (ret === "ok") {
          alert("Reserva cancelada com sucesso.");
          carregarHistoricoReservas(); // Atualiza a tabela
        } else {
          alert("Erro ao cancelar a reserva.");
        }
      })
      .catch(err => {
        console.error("Erro ao cancelar:", err);
        alert("Erro de conexão ao cancelar.");
      });
  }

  // Chamada inicial para carregar reservas ao abrir a página
  carregarHistoricoReservas();