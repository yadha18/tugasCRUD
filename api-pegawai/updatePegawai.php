<?php
 
include('koneksi.php');
 
$nama       = $_POST['nama'];
$no_pegawai = $_POST['no_pegawai'];
$alamat     = $_POST['alamat'];
$no_telp    = $_POST['no_telepon'];
 
if(!empty($nama) &amp;&amp; !empty($no_pegawai)){
 
    $sql = "UPDATE tb_pegawai set nama='$nama', alamat='$alamat', no_telepon='$no_telp' WHERE no_pegawai='$no_pegawai' ";
 
    $query = mysqli_query($conn,$sql);
 
    if(mysqli_affected_rows($conn) > 0){
        $data['status'] = true;
        $data['result'] = "Berhasil";
    }else{
        $data['status'] = false;
        $data['result'] = "Gagal";
    }
 
}else{
    $data['status'] = false;
    $data['result'] = "Gagal, Nomor Pegawai dan Nama tidak boleh kosong!";
}
 
 
print_r(json_encode($data));
 
 
 
 
?>