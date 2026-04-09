package com.eseict.reapi.dstr.facility.dto;

public record GisFacInfoDto(
    String facId,
    String facNm,
    String facClfyId,
    String facClfyNm,
    String mgtNo,
    String addrShort,
    Double lat,
    Double lng,
    boolean ptzYn
) {
    public static GisFacInfoDto of(String facId, String facNm, String facClfyId, String facClfyNm,
                                    String mgtNo, String addrShort, String yCrdnt, String xCrdnt, boolean ptzYn) {
        Double lat = parseDouble(yCrdnt);
        Double lng = parseDouble(xCrdnt);
        return new GisFacInfoDto(facId, facNm, facClfyId, facClfyNm, mgtNo, addrShort, lat, lng, ptzYn);
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Double.parseDouble(value); }
        catch (NumberFormatException e) { return null; }
    }
}
