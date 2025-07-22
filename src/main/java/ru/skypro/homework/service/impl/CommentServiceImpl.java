package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdRepository adRepository;

    @Override
    public List<CommentDTO> getCommentsForAd(Integer adId) {
        return commentRepository.findAllByAdId(adId)
                .stream()
                .map(commentMapper::commentEntityToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO addComment(Integer adId, CreateOrUpdateComment createComment, Authentication authentication) {
        // 1. Получаем текущего пользователя
        Users author = (Users) authentication.getPrincipal();

        // 2. Находим объявление по ID
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Ad not found"));

        // 3. Создаем и сохраняем комментарий
        Comment comment = new Comment();
        comment.setText(createComment.getText());
        comment.setAd(ad); // Устанавливаем связь с объявлением
        comment.setAuthor(author); // Устанавливаем автора
        comment.setCreatedAt(LocalDateTime.now());

        return commentMapper.commentEntityToCommentDTO(commentRepository.save(comment));
    }

    @Override
    public CommentDTO editComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment) {
        Comment existingComment = commentRepository.findByIdAndAdId(commentId, adId)  // Изменено на findByIdAndAdId
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        existingComment.setText(updateComment.getText());
        return commentMapper.commentEntityToCommentDTO(commentRepository.save(existingComment));
    }


    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteByIdAndAdId(commentId, adId);
    }
}