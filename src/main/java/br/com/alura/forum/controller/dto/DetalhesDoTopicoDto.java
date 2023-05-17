package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.forum.model.StatusTopico;
import br.com.alura.forum.model.Topico;

public class DetalhesDoTopicoDto {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private String nomeAutor;
    private StatusTopico statusTopico;
    private List<RespostaDto> resostas;

    public DetalhesDoTopicoDto(Topico topico) {
        this.id=topico.getId();
        this.titulo=topico.getTitulo();
        this.mensagem=topico.getMensagem();
        this.dataCriacao=topico.getDataCriacao();
        this.nomeAutor=topico.getAutor().getNome();
        this.statusTopico=topico.getStatus();
        this.resostas = new ArrayList<>();
        this.resostas.addAll(topico.getRespostas().stream().map(RespostaDto::new).collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public StatusTopico getStatusTopico() {
        return statusTopico;
    }

    public List<RespostaDto> getResostas() {
        return resostas;
    }
    
}
