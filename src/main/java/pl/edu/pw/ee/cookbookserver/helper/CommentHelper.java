package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class CommentHelper {

    private CommentRepository commentRepository;
    private UserHelper userHelper;

    @Autowired
    public CommentHelper(CommentRepository commentRepository, UserHelper userHelper) {
        this.commentRepository = commentRepository;
        this.userHelper = userHelper;
    }

    public CommentDto mapCommentToCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(userHelper.mapUserToBasicUserDto(comment.getAuthor()));
        commentDto.setContent(comment.getContent());
        commentDto.setCreationTime(comment.getCreationTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        Collection<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment1: comment.getComments()) {
            CommentDto commentDto1 = mapCommentToCommentDto(comment1);
            commentDtoList.add(commentDto1);
        }
        commentDto.setComments(commentDtoList);
        return commentDto;
    }

    public Comment getComment(Long id) throws ProcessingException {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new ProcessingException(Error.COMMENT_NOT_FOUND);
        }
        return comment;
    }
}
