package com.eseict.reapi.dstr.event.dto;

import java.util.List;

public record Paging<T>(long total, List<T> list) {}
