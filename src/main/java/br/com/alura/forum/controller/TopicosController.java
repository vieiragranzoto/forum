package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Curso;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.model.User;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import br.com.alura.forum.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> listar(@RequestParam(required = false) String nomeCurso,@PageableDefault(sort = "titulo", direction = Direction.ASC, page = 0, size = 10) Pageable pageable){

        if(nomeCurso == null){
            Page<Topico> topicos= topicoRepository.findAll(pageable);
            return TopicoDto.converter(topicos);
        }
        Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, pageable);
        return TopicoDto.converter(topicos);
    }

    @PostMapping
    @Transactional
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(@Valid @RequestBody TopicoForm form,
            UriComponentsBuilder uriComponentsBuilder) {
        Curso curso = cursoRepository.findByNome(form.getNomeCurso());
        Optional<User> usuario = userRepository.findById(form.getNomeUsuario());
        Topico topico = form.converter(curso, usuario.get());
        topicoRepository.save(topico);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizacaoTopicoForm form) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
