let produtos = JSON.parse(localStorage.getItem("produtos")) || [];
let carrinho = JSON.parse(localStorage.getItem("carrinho")) || [];

// Atualiza produtos e carrinho ao iniciar
atualizarListaProdutos();
atualizarCarrinho();

// Função para salvar no LocalStorage
function salvarDados() {
    localStorage.setItem("produtos", JSON.stringify(produtos));
    localStorage.setItem("carrinho", JSON.stringify(carrinho));
}

// Função para cadastrar produto
function cadastrarProduto() {
    const nome = document.getElementById("nome").value.trim();
    let preco = document.getElementById("preco").value.trim();
    preco = parseFloat(preco.replace(",", ".")); // Normaliza vírgula
    const estoque = parseInt(document.getElementById("estoque").value);

    if (!nome || isNaN(preco) || isNaN(estoque) || preco <= 0 || estoque <= 0) {
        alert("Preencha todos os campos corretamente!");
        return;
    }

    produtos.push({ nome, preco, estoque });
    salvarDados();
    atualizarListaProdutos();

    document.getElementById("nome").value = "";
    document.getElementById("preco").value = "";
    document.getElementById("estoque").value = "";
}

// Atualiza a lista de produtos
function atualizarListaProdutos() {
    const lista = document.getElementById("lista-produtos");
    lista.innerHTML = "";

    produtos.forEach((produto, index) => {
        const li = document.createElement("li");
        li.innerHTML = `
            <div>
                <strong>${produto.nome}</strong><br>
                R$ ${produto.preco.toFixed(2)} | Estoque: ${produto.estoque}
            </div>
            <button onclick="adicionarAoCarrinho(${index})" ${produto.estoque === 0 ? "disabled" : ""}>
                ${produto.estoque === 0 ? "Esgotado" : "Comprar"}
            </button>
        `;
        lista.appendChild(li);
    });
}

// Filtrar produtos pelo nome
function filtrarProdutos() {
    const busca = document.getElementById("busca").value.toLowerCase();
    const lista = document.getElementById("lista-produtos");
    lista.innerHTML = "";

    produtos.forEach((produto, index) => {
        if (produto.nome.toLowerCase().includes(busca)) {
            const li = document.createElement("li");
            li.innerHTML = `
                <div>
                    <strong>${produto.nome}</strong><br>
                    R$ ${produto.preco.toFixed(2)} | Estoque: ${produto.estoque}
                </div>
                <button onclick="adicionarAoCarrinho(${index})" ${produto.estoque === 0 ? "disabled" : ""}>
                    ${produto.estoque === 0 ? "Esgotado" : "Comprar"}
                </button>
            `;
            lista.appendChild(li);
        }
    });
}

// Adicionar produto ao carrinho
function adicionarAoCarrinho(index) {
    const produto = produtos[index];

    if (produto.estoque <= 0) {
        alert("Produto sem estoque!");
        return;
    }

    const itemExistente = carrinho.find(item => item.nome === produto.nome);

    if (itemExistente) {
        itemExistente.quantidade++;
    } else {
        carrinho.push({ nome: produto.nome, preco: produto.preco, quantidade: 1 });
    }

    produto.estoque--;
    salvarDados();
    atualizarListaProdutos();
    atualizarCarrinho();
}

// Atualizar carrinho
function atualizarCarrinho() {
    const lista = document.getElementById("carrinho");
    lista.innerHTML = "";

    let total = 0;

    carrinho.forEach((item, index) => {
        const li = document.createElement("li");
        li.innerHTML = `
            ${item.nome} | ${item.quantidade} un. | R$ ${(item.preco * item.quantidade).toFixed(2)}
            <button onclick="removerDoCarrinho(${index})">❌</button>
        `;
        lista.appendChild(li);
        total += item.preco * item.quantidade;
    });

    document.getElementById("total").textContent = `Total: R$ ${total.toFixed(2)}`;
}

// Remover item do carrinho
function removerDoCarrinho(index) {
    const item = carrinho[index];
    const produto = produtos.find(p => p.nome === item.nome);

    if (produto) {
        produto.estoque += item.quantidade;
    }

    carrinho.splice(index, 1);
    salvarDados();
    atualizarListaProdutos();
    atualizarCarrinho();
}

// Finalizar compra
function finalizarCompra() {
    if (carrinho.length === 0) {
        alert("Seu carrinho está vazio!");
        return;
    }

    let total = carrinho.reduce((acc, item) => acc + item.preco * item.quantidade, 0);

    alert(`🎉 Compra finalizada com sucesso!\nTotal: R$ ${total.toFixed(2)}`);
    carrinho = [];
    salvarDados();
    atualizarCarrinho();
    atualizarListaProdutos(); // Garante atualização dos botões
}

