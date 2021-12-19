<?php
 
include('koneksi.php');
 
$no_pegawai = $_POST['no_pegawai'];
 
if(!empty($nis)){
    $sql = "DELETE FROM tb_pegawai WHERE no_pegawai='$no_pegawai' ";
 
    $query = mysqli_query($conn,$sql);
 
    $data['status'] = true;
    $data['result'] = 'Berhasil';
}else{
    $data['status'] = false;
    $data['result'] = 'Gagal';
}
 
print_r(json_encode($data));
 
 
?>