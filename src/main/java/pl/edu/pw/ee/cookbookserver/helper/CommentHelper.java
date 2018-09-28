package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;

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

    public Collection<CommentDto> mapCommentToCommentDto(Iterable<Comment> comments) {
        if (comments == null) {
            return null;
        }
        Collection<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment: comments) {
            CommentDto commentDto = mapCommentToCommentDto(comment);
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    public CommentDto mapCommentToCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(userHelper.mapUserToBasicUserDto(comment.getAuthor()));
        commentDto.setContent(comment.getContent());
        commentDto.setCreationTime(comment.getCreationTime());
        commentDto.setModificationTime(comment.getModificationTime());
        commentDto.setCommentsCount((long) comment.getComments().size());
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
