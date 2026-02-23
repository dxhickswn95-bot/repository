package com.example.demo.member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PlusMemberDAO implements MemberDAO {

    private final JdbcTemplate jdbcTemplate;

    public PlusMemberDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberDTO> memberRowMapper = (rs, rowNum) -> {
        MemberDTO dto = new MemberDTO();
        dto.setMemberId(rs.getString("member_id"));
        dto.setSocialType(rs.getString("social_type"));
        dto.setNickname(rs.getString("nickname"));
        dto.setProfileImage(rs.getString("profile_image"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));

        // ⭐ 추가
        dto.setRole(rs.getString("role"));

        // password는 보통 화면에 뿌릴 일이 없어서 굳이 매핑 안 해도 되지만,
        // 필요하면 아래도 가능
        // dto.setPassword(rs.getString("password"));

        return dto;
    };

    @Override
    public MemberDTO findById(String memberId) {
        String sql = "SELECT member_id, social_type, nickname, profile_image, created_at, role " +
                     "FROM member WHERE member_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void saveOrUpdateSocial(MemberDTO dto) {
        MemberDTO existing = findById(dto.getMemberId());

        if (existing == null) {
            String insertSql =
                "INSERT INTO member (member_id, social_type, nickname, profile_image) " +
                "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSql,
                    dto.getMemberId(),
                    dto.getSocialType(),
                    dto.getNickname(),
                    dto.getProfileImage());
        } else {
            // role/password는 건드리지 않음(덮어쓰기 방지)
            String updateSql =
                "UPDATE member SET nickname = ?, profile_image = ? WHERE member_id = ?";
            jdbcTemplate.update(updateSql,
                    dto.getNickname(),
                    dto.getProfileImage(),
                    dto.getMemberId());
        }
    }

    @Override
    public int updateNickname(String memberId, String nickname) {
        String sql = "UPDATE member SET nickname = ? WHERE member_id = ?";
        return jdbcTemplate.update(sql, nickname, memberId);
    }

    @Override
    public int deleteById(String memberId) {
        String sql = "DELETE FROM member WHERE member_id = ?";
        return jdbcTemplate.update(sql, memberId);
    }

    // ⭐ 추가: 로컬 로그인 (local 계정만 허용)
    @Override
    public MemberDTO findLocalByIdAndPassword(String memberId, String password) {
        String sql = "SELECT member_id, social_type, nickname, profile_image, created_at, role " +
                     "FROM member " +
                     "WHERE member_id = ? AND password = ? AND social_type = 'local'";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, memberId, password);
        } catch (Exception e) {
            return null;
        }
    }
}
