package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Municao;
import com.example.demo.entities.Usuario;
import com.example.demo.Expections.BusinessException;
import com.example.demo.entities.Entrega;
import com.example.demo.repository.EntregaRepository;
import com.example.demo.repository.MunicaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

@Service
public class MunicaoService {

    @Autowired
    private MunicaoRepository municaoRepository;
    
    @Autowired
    private EntregaRepository entregaRepository;
    
    @Autowired
    private UserActionService userActionService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    
    
    public List<Municao> getAllMunicoes() {
        return municaoRepository.findAllOrderedById();
    }

    public Municao getMunicao(int id) throws NotFoundException {
        return municaoRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void saveMunicao(Municao municao) throws BusinessException, NotFoundException {
        // Valide os campos obrigatórios da munição, por exemplo, o tipo, calibre, quantidade, etc.
        if (municao.getTipo() == null || municao.getCalibre() == null || municao.getQuantidade() <= 0) {
            throw new BusinessException("Os campos obrigatórios da munição não foram preenchidos corretamente.");
        }

        // Certifique-se de que o usuário está autenticado
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2();
        if (usuarioLogado == null) {
            throw new NotFoundException();
        }

        // Registre a ação do usuário ao cadastrar a munição
        String username = usuarioLogado.getNome();
        userActionService.registerUserAction(username, "Cadastrou uma nova munição");

        // Salve a munição no repositório
        municaoRepository.save(municao);
    }


    public Municao updateMunicao(Municao municao, int id) throws NotFoundException {
        Municao atualizada = municaoRepository.findById(id).orElseThrow(NotFoundException::new);

        atualizada.setTipo(municao.getTipo());
        atualizada.setCalibre(municao.getCalibre());
        atualizada.setQuantidade(municao.getQuantidade());
        atualizada.setPeso(municao.getPeso());
        atualizada.setCoeficienteBalistico(municao.getCoeficienteBalistico());
        atualizada.setDataFabricacao(municao.getDataFabricacao());
        atualizada.setDataValidade(municao.getDataValidade());
        
        // Registro da ação do usuário ao editar a munição
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Editou uma munição com ID: " + id);

        municaoRepository.save(atualizada);
        return atualizada;
    }

    private boolean isMunicaoAssociadaEntrega(int municaoId) {
        // Verifique se existe alguma entrega associada à munição com o ID especificado
        List<Entrega> entregas = entregaRepository.findByMunicaoId(municaoId);

        // Se a lista de entregas não estiver vazia, significa que há entregas associadas
        return !entregas.isEmpty();
    }
    
    public Municao deleteMunicao(int id) throws EntityNotFoundException, BusinessException, NotFoundException {
        Municao deletada = municaoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        
        // Verificar se a munição está associada a alguma entrega
        if (isMunicaoAssociadaEntrega(id)) {
            throw new BusinessException("Não é possível excluir a munição porque está associada a uma entrega.");
        }
        
        // Registro da ação do usuário ao excluir a munição
        Usuario usuarioLogado = usuarioService.getUsuarioLogado2(); 
        String username = usuarioLogado != null ? usuarioLogado.getNome() : "Usuário Desconhecido";
        userActionService.registerUserAction(username, "Excluiu uma munição com ID: " + id);


        municaoRepository.delete(deletada);
        return deletada;
    }

    
    public Page<Municao> buscarMunicoesPorFiltro(String termo, Pageable pageable) {
        Specification<Municao> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

	    predicates.add(cb.like(cb.lower(root.get("tipo")), "%" + termo.toLowerCase() + "%"));

	    predicates.add(cb.like(cb.lower(root.get("calibre")), "%" + termo.toLowerCase() + "%"));


            // Busca por quantidade
            try {
                int quantidade = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("quantidade"), quantidade));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
            }

	    try {
            	float peso = Float.parseFloat(termo);
            	predicates.add(cb.equal(root.get("peso"), peso));
            } catch (NumberFormatException e) {
            	// Ignorar se o termo não for um número válido
            }

	    try {
            	float coeficienteBalistico = Float.parseFloat(termo);
            	predicates.add(cb.equal(root.get("coeficienteBalistico"), coeficienteBalistico));
            } catch (NumberFormatException e) {
            	// Ignorar se o termo não for um número válido
            }


            try {
                LocalDate dataFabricacao = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataFabricacao"), dataFabricacao));
            } catch (DateTimeParseException e) {
                // Ignorar se o termo não for uma data válida
            }

            try {
                LocalDate dataValidade = LocalDate.parse(termo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                predicates.add(cb.equal(root.get("dataValidade"), dataValidade));
            } catch (DateTimeParseException e) {
                // Ignorar se o termo não for uma data válida
            }

            // Busca por ID
            try {
                int id = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
                // Ignorar se o termo não for um número válido
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return municaoRepository.findAll(spec, pageable);
    }
    
    public int buscarTotalMunicoesEmEstoque() {
        List<Municao> municoes = municaoRepository.findAll();
        int total = 0;
        for (Municao municao : municoes) {
            total += municao.getQuantidade();
        }
        return total;
    }
    
    public List<Map<String, Object>> getDadosMunicoes() {
        List<Object[]> dados = municaoRepository.findTipoCalibreQuantidade(); // Consulta personalizada no repositório
        List<Map<String, Object>> dadosMunicoes = new ArrayList<>();

        for (Object[] dado : dados) {
            Map<String, Object> municaoData = new HashMap<>();
            municaoData.put("tipo", dado[0]);
            municaoData.put("calibre", dado[1]);
            municaoData.put("quantidade", dado[2]);
            dadosMunicoes.add(municaoData);
        }

        return dadosMunicoes;
    }

	public Optional<Municao> findById(int id) {
		return municaoRepository.findById(id); 
	}

    public Page<Municao> getAllMunicoes(Pageable pageable) {
        return municaoRepository.findAll(pageable);
    }
}