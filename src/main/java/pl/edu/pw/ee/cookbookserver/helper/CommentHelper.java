package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.entity.Comment;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class CommentHelper {

    private UserHelper userHelper;

    @Autowired
    public CommentHelper(UserHelper userHelper) {
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
}
