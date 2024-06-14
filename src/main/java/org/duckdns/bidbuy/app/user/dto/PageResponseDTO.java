package org.duckdns.bidbuy.app.user.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> mySalesResponses;

    private Pageable pageable;

    private List<Integer> pageNumList;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    public PageResponseDTO(List<E> mySalesResponses, Pageable pageable, long total) {
        this.mySalesResponses = mySalesResponses;
        this.pageable = pageable;
        this.totalCount = (int) total;

        // 끝페이지 end
        int end = (int) (Math.ceil((pageable.getPageNumber()+1) / 10.0)) * 10;
        int start = end - 9;

        // 진짜 마지막페이지
        int last = (int) (Math.ceil(totalCount / (double) pageable.getPageSize()));

        end = end > last ? last : end;

        this.prev = start > 1;
        this.next = totalCount > end * pageable.getPageSize();
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().toList();

        this.prevPage = prev ? start - 1 : 0;
        this.nextPage = next ? end + 1 : 0;
    }
}
