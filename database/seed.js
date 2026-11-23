
const database = db.getSiblingDB("mgologisticadb00");

// ==========================================
// CRIAR COLLECTIONS CASO NÃO EXISTAM
// ==========================================

const collections = [
    "clientes",
    "motoristas",
    "produtos",
    "veiculos",
    "pedidos",
    "entregas"
];

const collectionsExistentes = database.getCollectionNames();

collections.forEach(collection => {
    if (!collectionsExistentes.includes(collection)) {
        database.createCollection(collection);
        print(`Collection criada: ${collection}`);
    } else {
        print(`Collection já existe: ${collection}`);
    }
});

// ==========================================
// CONFIGURAÇÕES
// ==========================================

const now = new Date();
const day = 86400000;

// ==========================================
// LIMPAR COLLECTIONS
// ==========================================

collections.forEach(collection => {
    database.getCollection(collection).deleteMany({});
});

print("Collections limpas com sucesso!");

// ==========================================
// CLIENTES
// ==========================================

const clientes = Array.from({ length: 10 }, (_, i) => ({
    _id: ObjectId(),

    nome: [
        "Joao da Silva",
        "Maria Oliveira",
        "Carlos Santos",
        "Ana Costa",
        "Pedro Ferreira",
        "Fernanda Lima",
        "Ricardo Alves",
        "Juliana Martins",
        "Roberto Souza",
        "Camila Rodrigues"
    ][i],

    documento: `1234567890${i}`,
    telefone: `8199999000${i}`,
    email: `cliente${i + 1}@email.com`,
    endereco: `Rua das Flores, ${100 + i}`,

    cidade: [
        "Recife",
        "Olinda",
        "Jaboatao dos Guararapes",
        "Paulista",
        "Camaragibe"
    ][i % 5],

    estado: "PE",
    createdAt: now,
    updatedAt: now
}));

database.clientes.insertMany(clientes);

print("Clientes inseridos: " + clientes.length);

// ==========================================
// PRODUTOS
// ==========================================

const produtosDados = [
    ["Notebook Dell", "Notebook Core i5 16GB", 2.1, 2, 36, 25, 3500],
    ["Monitor Samsung", "Monitor LED Full HD", 3.5, 40, 55, 15, 899.90],
    ["Teclado Mecanico", "Teclado RGB USB", 0.8, 5, 15, 45, 250],
    ["Mouse Logitech", "Mouse sem fio", 0.2, 4, 7, 12, 120],
    ["Impressora HP", "Multifuncional", 5.5, 20, 40, 45, 1200],
    ["Smartphone Samsung", "Smartphone 128GB", 0.3, 16, 8, 2, 2200],
    ["Tablet Lenovo", "Tablet Android", 0.6, 25, 16, 1, 1500],
    ["Cadeira Gamer", "Cadeira ergonomica", 15, 120, 65, 70, 950],
    ["Roteador TP-Link", "Roteador Wi-Fi", 0.5, 8, 20, 15, 320],
    ["HD Externo", "HD externo 1TB", 0.25, 12, 8, 2, 450]
];

const produtos = produtosDados.map(p => ({
    _id: ObjectId(),
    nome: p[0],
    descricao: p[1],
    peso: NumberDecimal(p[2].toString()),
    altura: NumberDecimal(p[3].toString()),
    largura: NumberDecimal(p[4].toString()),
    comprimento: NumberDecimal(p[5].toString()),
    valor: NumberDecimal(p[6].toString()),
    createdAt: now,
    updatedAt: now
}));

database.produtos.insertMany(produtos);

print("Produtos inseridos: " + produtos.length);

// ==========================================
// MOTORISTAS
// ==========================================

const motoristasDados = [
    ["Marcos Antonio", "11122233344", "12345678901", "ATIVO"],
    ["Paulo Henrique", "22233344455", "23456789012", "ATIVO"],
    ["Anderson Lima", "33344455566", "34567890123", "FERIAS"],
    ["Robson Pereira", "44455566677", "45678901234", "ATIVO"],
    ["Eduardo Souza", "55566677788", "56789012345", "INATIVO"]
];

const motoristas = motoristasDados.map((m, i) => ({
    _id: ObjectId(),
    nome: m[0],
    cpf: m[1],
    telefone: `8198888000${i + 1}`,
    cnh: m[2],
    validadeCnh: new Date("2029-12-31T00:00:00Z"),
    status: m[3],
    createdAt: now,
    updatedAt: now
}));

database.motoristas.insertMany(motoristas);

print("Motoristas inseridos: " + motoristas.length);

// ==========================================
// VEICULOS
// ==========================================

const veiculosDados = [
    ["ABC1D23", "Delivery 11.180", "Volkswagen", 5000, "DISPONIVEL"],
    ["DEF2G34", "Accelo 1016", "Mercedes-Benz", 6000, "EM_USO"],
    ["GHI3J45", "Fiorino", "Fiat", 650, "DISPONIVEL"],
    ["JKL4M56", "Sprinter", "Mercedes-Benz", 1800, "MANUTENCAO"],
    ["NOP5Q67", "Master", "Renault", 1500, "DISPONIVEL"]
];

const veiculos = veiculosDados.map(v => ({
    _id: ObjectId(),
    placa: v[0],
    modelo: v[1],
    marca: v[2],
    capacidadeCarga: NumberDecimal(v[3].toString()),
    status: v[4],
    createdAt: now,
    updatedAt: now
}));

database.veiculos.insertMany(veiculos);

print("Veiculos inseridos: " + veiculos.length);

// ==========================================
// PEDIDOS
// ==========================================

const pedidosDados = [
    { cliente: 0, itens: [[0, 1], [2, 1]], status: "PENDENTE" },
    { cliente: 1, itens: [[1, 1], [3, 1]], status: "APROVADO" },
    { cliente: 2, itens: [[4, 1]], status: "EM_SEPARACAO" },
    { cliente: 3, itens: [[5, 2]], status: "ENVIADO" },
    { cliente: 4, itens: [[6, 1]], status: "CONCLUIDO" },
    { cliente: 5, itens: [[7, 1]], status: "APROVADO" },
    { cliente: 6, itens: [[8, 2]], status: "CANCELADO" },
    { cliente: 7, itens: [[9, 2]], status: "ENVIADO" },
    { cliente: 8, itens: [[2, 1], [3, 1]], status: "CONCLUIDO" },
    { cliente: 9, itens: [[0, 1], [6, 1]], status: "EM_SEPARACAO" }
];

const pedidos = [];

pedidosDados.forEach((dados, i) => {

    const itens = dados.itens.map(([produtoIndex, quantidade]) => {

        const produto = produtos[produtoIndex];

        const valorCentavos = Math.round(
            Number(produto.valor.toString()) * 100
        );

        const subtotalCentavos = valorCentavos * quantidade;

        return {
            produtoId: produto._id.toString(),
            nomeProduto: produto.nome,
            quantidade,

            valorUnitario: NumberDecimal(
                (valorCentavos / 100).toFixed(2)
            ),

            subtotal: NumberDecimal(
                (subtotalCentavos / 100).toFixed(2)
            )
        };
    });

    const totalCentavos = itens.reduce(
        (total, item) => {
            return total + Math.round(
                Number(item.subtotal.toString()) * 100
            );
        },
        0
    );

    pedidos.push({
        _id: ObjectId(),
        clienteId: clientes[dados.cliente]._id.toString(),

        dataPedido: new Date(
            now.getTime() - i * day
        ),

        valorTotal: NumberDecimal(
            (totalCentavos / 100).toFixed(2)
        ),

        status: dados.status,
        observacao: `Pedido de demonstracao ${i + 1}`,
        itens,
        createdAt: now,
        updatedAt: now
    });
});

print("Pedidos preparados: " + pedidos.length);

const resultadoPedidos = database.pedidos.insertMany(pedidos);

print(
    "Pedidos inseridos: " +
    Object.keys(resultadoPedidos.insertedIds).length
);

// ==========================================
// ENTREGAS
// ==========================================

const entregasDados = [
    { motorista: 0, veiculo: 0, status: "AGUARDANDO" },
    { motorista: 1, veiculo: 1, status: "EM_TRANSITO" },
    { motorista: 0, veiculo: 0, status: "AGUARDANDO" },
    { motorista: 3, veiculo: 2, status: "EM_TRANSITO" },
    { motorista: 1, veiculo: 4, status: "ENTREGUE" },
    { motorista: 3, veiculo: 2, status: "AGUARDANDO" },
    { motorista: 0, veiculo: 0, status: "CANCELADA" },
    { motorista: 3, veiculo: 4, status: "EM_TRANSITO" },
    { motorista: 1, veiculo: 0, status: "ENTREGUE" },
    { motorista: 0, veiculo: 4, status: "AGUARDANDO" }
];

const entregas = entregasDados.map((dados, i) => {

    const cliente = clientes[pedidosDados[i].cliente];
    const status = dados.status;

    return {
        _id: ObjectId(),

        pedidoId: pedidos[i]._id.toString(),
        motoristaId: motoristas[dados.motorista]._id.toString(),
        veiculoId: veiculos[dados.veiculo]._id.toString(),

        enderecoEntrega: cliente.endereco,
        cidade: cliente.cidade,
        estado: cliente.estado,

        dataSaida: ["EM_TRANSITO", "ENTREGUE"].includes(status)
            ? new Date(now.getTime() - day)
            : null,

        dataEntrega: status === "ENTREGUE"
            ? now
            : null,

        status,
        observacao: `Entrega de demonstracao ${i + 1}`,
        createdAt: now,
        updatedAt: now
    };
});

print("Entregas preparadas: " + entregas.length);

const resultadoEntregas = database.entregas.insertMany(entregas);

print(
    "Entregas inseridas: " +
    Object.keys(resultadoEntregas.insertedIds).length
);

// ==========================================
// INDICES
// ==========================================

database.clientes.createIndex(
    { documento: 1 },
    { unique: true }
);

database.motoristas.createIndex(
    { cpf: 1 },
    { unique: true }
);

database.veiculos.createIndex(
    { placa: 1 },
    { unique: true }
);

database.entregas.createIndex(
    { pedidoId: 1 },
    { unique: true }
);

print("Indices criados com sucesso!");

// ==========================================
// RESULTADO FINAL
// ==========================================

print("\n========== SEED CONCLUIDO ==========");

let totalRegistros = 0;

collections.forEach(collection => {

    const quantidade = database
        .getCollection(collection)
        .countDocuments();

    totalRegistros += quantidade;

    print(`${collection}: ${quantidade} registros`);
});

print("------------------------------------");
print("TOTAL DE REGISTROS: " + totalRegistros);
print("====================================");
